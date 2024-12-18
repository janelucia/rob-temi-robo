package de.fhkiel.temi.robogguide

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.robotemi.sdk.BatteryData
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnBatteryStatusChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import com.robotemi.sdk.map.MapDataModel
import com.robotemi.sdk.permission.OnRequestPermissionResultListener
import com.robotemi.sdk.permission.Permission
import de.fhkiel.temi.robogguide.database.DatabaseHelper
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.logic.isSpeaking
import de.fhkiel.temi.robogguide.logic.processQueue
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.logic.ttsQueue
import de.fhkiel.temi.robogguide.models.GuideState
import de.fhkiel.temi.robogguide.ui.logic.SetupViewModel
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import de.fhkiel.temi.robogguide.ui.theme.Rob_Temi_Robo_UITheme
import de.fhkiel.temi.robogguide.ui.theme.components.CustomTopAppBar
import de.fhkiel.temi.robogguide.ui.theme.components.GuideNavigationButton
import de.fhkiel.temi.robogguide.ui.theme.pages.EndPage
import de.fhkiel.temi.robogguide.ui.theme.pages.Guide
import de.fhkiel.temi.robogguide.ui.theme.pages.GuideExhibition
import de.fhkiel.temi.robogguide.ui.theme.pages.GuideSelector
import de.fhkiel.temi.robogguide.ui.theme.pages.Home
import de.fhkiel.temi.robogguide.ui.theme.pages.Setup
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

const val REQUEST_CODE_MAP = 10
const val REQUEST_CODE_SETTINGS = 1

class MainActivity : ComponentActivity(), OnRobotReadyListener, OnRequestPermissionResultListener,
    OnGoToLocationStatusChangedListener,
    OnUserInteractionChangedListener,
    Robot.TtsListener,
    OnBatteryStatusChangedListener {

    private lateinit var setupViewModel: SetupViewModel
    private lateinit var tourViewModel: TourViewModel

    /** Navigation controller for the app */
    private lateinit var navController: NavHostController

    private var mRobot: Robot? = null
    private lateinit var database: DatabaseHelper
    private lateinit var tourManager: TourManager
    private val handler = Handler(Looper.getMainLooper())
    private var isUserInteracting = false
    private lateinit var sharedPreferences: SharedPreferences
    private var dialogShown = false

    private val singleThreadExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val activity: Activity = this

    private var interactionDialog: AlertDialog? = null

    private val noInteractionRunnable = Runnable {
        if (!isUserInteracting) {
            showInteractionDialog()
        }
    }

    private val returnHomeRunnable = Runnable {
        gotoHomeBase()
        interactionDialog?.dismiss()
        navController.navigate("homePage")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ---- VIEW MODEL SETUP ----
        tourViewModel = TourViewModel()
        setupViewModel = SetupViewModel(application)

        // ---- SHARED PREFERENCES ----
        sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // ---- DATABASE ACCESS ----
        val databaseName = "roboguide-präsi.db"
        database = DatabaseHelper(this, databaseName)

        try {
            database.initializeDatabase() // Initialize the database and copy it from assets
            tourManager = TourManager(database.getDatabase())

        } catch (e: IOException) {
            e.printStackTrace()
            tourManager = TourManager(null) // Initialize with null to simulate error
            tourManager.error.value = e
        }

        setContent {
            navController = rememberNavController()

            val isSetupComplete by setupViewModel.isSetupComplete.observeAsState(false)

            if (isSetupComplete) {
                Rob_Temi_Robo_UITheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            CustomTopAppBar(
                                navController, tourViewModel, activity,
                                mRobot
                            )
                        },
                        bottomBar = {
                            GuideNavigationButton(
                                navController,
                                tourViewModel,
                                mRobot
                            )
                        }
                    ) { innerPadding ->
                        NavHost(navController, startDestination = "homePage") {
                            composable("homePage") {
                                Home(
                                    innerPadding,
                                    navController,
                                    mRobot,
                                    tourManager
                                )
                            }
                            composable("guideSelector") {
                                GuideSelector(
                                    innerPadding,
                                    navController,
                                    tourManager,
                                    tourViewModel
                                )
                            }
                            composable("guide") {
                                Guide(
                                    innerPadding,
                                    mRobot,
                                    tourViewModel,
                                    tourManager,
                                    navController,
                                    setupViewModel
                                )
                            }
                            composable("guideExhibition") {
                                GuideExhibition(
                                    innerPadding,
                                    tourManager,
                                    tourViewModel,
                                    navController
                                )
                            }
                            composable("detailedExhibit") {
                                Guide(
                                    innerPadding,
                                    mRobot,
                                    tourViewModel,
                                    tourManager,
                                    navController,
                                    setupViewModel
                                )
                            }
                            composable("endPage") {
                                EndPage(
                                    innerPadding,
                                    navController,
                                    tourViewModel,
                                    mRobot
                                )
                            }
                        }
                    }
                }
            } else {
                Setup(setupViewModel, tourManager)
            }

        }

    }

    override fun onStart() {
        super.onStart()
        val robot = Robot.getInstance()
        robot.addOnRobotReadyListener(this)
        robot.addOnRequestPermissionResultListener(this)
        robot.addOnGoToLocationStatusChangedListener(this)
        robot.addOnUserInteractionChangedListener(this)
        robot.addTtsListener(this)
        robot.addOnBatteryStatusChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        Robot.getInstance().removeOnRobotReadyListener(this)
        Robot.getInstance().removeOnRequestPermissionResultListener(this)
        Robot.getInstance().addOnGoToLocationStatusChangedListener(this)
        Robot.getInstance().removeOnUserInteractionChangedListener(this)
        Robot.getInstance().removeTtsListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.closeDatabase()
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {

            Log.i("MainActivity", "Robot is ready")
            mRobot = Robot.getInstance()

            // request needed Permissions
            requestPermissionsIfNeeded(Permission.MAP, REQUEST_CODE_MAP)
            requestPermissionsIfNeeded(Permission.SETTINGS, REQUEST_CODE_SETTINGS)

            // enable the detectionMode
            mRobot?.setDetectionModeOn(on = false, distance = 0.5f)
            Log.i("MainActivity", "Detection mode is enabled ${mRobot?.detectionModeOn}")

            mRobot?.isKioskModeOn()?.let { isKioskModeOn ->
                Log.i("MainActivity", "Kiosk mode is enabled $isKioskModeOn")
            }

            if (sharedPreferences.getBoolean("kiosk_mode", false)) {
                mRobot?.requestToBeKioskApp()
            }

            // ---- DISABLE TEMI UI ELEMENTS ---
            mRobot?.hideTopBar()        // hide top action bar

            // hide pull-down bar only in live mode
            if (!setupViewModel.isDebugFlagEnabled.value!!) {
                val activityInfo: ActivityInfo =
                    packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
                Robot.getInstance().onStart(activityInfo)
            }


            // Observe the ttsQueue
            ttsQueue.observe(this) { queue ->
                Log.d("MainActivity", "Queue changed: ${queue.size}")
                if (queue.isNotEmpty() && !isSpeaking.value!!) {
                    Log.d("MainActivity", "Process queue")
                    processQueue(mRobot)
                }
            }

            mRobot?.batteryData?.isCharging?.let { isCharging ->
                tourViewModel.isAtHomeBase.value = isCharging
            }

            setupViewModel.robotIsReady()
        }
    }

    /**
     * Uses temi sdk function to go to home base
     */
    private fun gotoHomeBase() {
        robotSpeakText(mRobot, "Ich fahre jetzt zur Aufladestation!", false)
        mRobot?.goTo(location = "home base")
    }

    /**
     * Gets the [MapDataModel] of the robot and shows its data in Logcat
     */
    private fun showMapData() {
        singleThreadExecutor.execute {
            getMap()?.let { mapDataModel ->
                Log.i("Map-mapImage", mapDataModel.mapImage.typeId)
                Log.i("Map-mapId", mapDataModel.mapId)
                Log.i("Map-mapInfo", mapDataModel.mapInfo.toString())
                Log.i("Map-greenPaths", mapDataModel.greenPaths.toString())
                Log.i("Map-virtualWalls", mapDataModel.virtualWalls.toString())
                Log.i("Map-locations", mapDataModel.locations.toString())
            }
            return@execute
        }

        Log.i("Map-List", "${mRobot?.getMapList()}")
    }

    /**
     * Gets the robot [MapDataModel] data.
     * Therefor it checks if the required permission is granted
     * @return      [MapDataModel] of robots map data, or null if no robot found, no permissions are granted or data is null.
     */
    private fun getMap(): MapDataModel? {
        // check if permission is missing
        requestPermissionsIfNeeded(Permission.MAP, REQUEST_CODE_MAP)
        return mRobot?.getMapData()
    }


    /**
     * Requests a permission
     * @param   permission      [Permission] robot temi permission to request
     * @param   requestCode     [Int] request code to identify request response
     * @return                  true if [Permission] was requested, false if [Permission] is already granted.
     */
    @Suppress("SameParameterValue")
    private fun requestPermissionsIfNeeded(permission: Permission, requestCode: Int): Boolean {
        Log.d("PERMISSION", "Request permission $permission with request code $requestCode")
        if (mRobot?.checkSelfPermission(permission) == Permission.GRANTED) {
            Log.d("PERMISSION", "Permission $permission already granted")
            return false
        } else {
            Log.d("PERMISSION", "Requesting permission $permission")
            mRobot?.requestPermissions(listOf(permission), requestCode)
            // return is permission granted?
            return true
        }
    }


    /**
     * Permission request callback
     * @param   permission      [Permission] that was requested.
     * @param   grantResult     [Int] with request result (see [Permission] constants)
     * @param   requestCode     [Int] custom request code set in [Permission] request
     */
    override fun onRequestPermissionResult(
        permission: Permission,
        grantResult: Int,
        requestCode: Int,
    ) {
        Log.d(
            "PERMISSION",
            "permission $permission with request code $requestCode with result = $grantResult"
        )

        when (permission) {
            Permission.MAP -> when (requestCode) {
                REQUEST_CODE_MAP -> {
                    showMapData()
                }
            }
            Permission.SETTINGS -> {
                if (grantResult == Permission.GRANTED) {
                    Log.i("MainActivity", "Settings permission granted")
                } else {
                    Log.e("MainActivity", "Settings permission denied")
                }
            }
            else -> {
                // do nothing
            }
        }
    }

    override fun onUserInteraction(isInteracting: Boolean) {

        isUserInteracting = isInteracting
        Log.i("MainActivity", "User is interacting: $isInteracting")
        if (isInteracting) {
            handler.removeCallbacks(noInteractionRunnable)
            handler.removeCallbacks(returnHomeRunnable)
            dialogShown = false
        } else {
            handler.postDelayed(noInteractionRunnable, 1000 * 60 * 5) // 5 minutes
        }
    }

    private fun showInteractionDialog() {
        Log.i("MainActivity", "Show interaction dialog")

        assert(mRobot != null)
        val oldVolume = mRobot?.volume!!
        val isCharging = mRobot?.batteryData?.isCharging

        // check if dialog is already shown and robot is not charging
        if (!dialogShown && !isCharging!!) {
            dialogShown = true
            // scream
            mRobot?.volume = 7
            robotSpeakText(mRobot, "Hallo, werde ich noch gebraucht?", false)

            interactionDialog = AlertDialog.Builder(this)
                .setOnDismissListener {
                    isUserInteracting = true
                    dialogShown = false
                    mRobot?.volume = oldVolume
                    handler.removeCallbacks(returnHomeRunnable)
                }
                .setTitle("Tour fortführen?")
                .setMessage("Ich habe längere Zeit keine Interaktion festgestellt. Möchtest du die Tour fortsetzen oder darf der Roboter zurück zur Homebase?\nDu hast 2 Minuten Zeit, bevor ich nach Hause fahre.")
                .setPositiveButton("Tour fortführen") { _, _ ->
                    isUserInteracting = true
                    handler.removeCallbacks(returnHomeRunnable)
                    mRobot?.volume = oldVolume
                }
                .setNegativeButton("Tour beenden und Roboter zur Homebase schicken") { _, _ ->
                    gotoHomeBase()
                    isUserInteracting = true
                    handler.removeCallbacks(returnHomeRunnable)
                    navController.navigate("homePage")
                    mRobot?.volume = oldVolume
                }
                .show()

            handler.postDelayed(returnHomeRunnable, 1000 * 12 * 60) // 2 Min
        }
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        Log.d(
            "Transfer",
            "Mein GoTO Status $status GuideStatus ${tourViewModel.guideState.value} Description $description + ID $descriptionId"
        )
        when (status) {
            OnGoToLocationStatusChangedListener.START -> {
                if (tourViewModel.guideState.value == GuideState.TransferStart) {
                    tourViewModel.updateGuideState(GuideState.TransferGoing)
                    Log.d(
                        "Transfer",
                        "Ich beginne mich zu bewegen -> ${tourViewModel.guideState.value}"
                    )
                }
            }

            OnGoToLocationStatusChangedListener.COMPLETE -> {
                if (tourViewModel.guideState.value == GuideState.TransferGoing) {
                    // Roboter erreicht Ziel
                    tourViewModel.updateGuideState(GuideState.Exhibit)
                    Log.d(
                        "Transfer",
                        "Ich habe mein Ziel erreicht -> ${tourViewModel.guideState.value}"
                    )
                }
            }

            OnGoToLocationStatusChangedListener.ABORT -> {
                Log.d(
                    "Transfer",
                    "NACH ABORT: Mein GoTO Status $status GuideStatus ${tourViewModel.guideState.value} Description ID $description"
                )

                if (descriptionId == 0) {
                    // general abort -> nothing to do
                    return
                }

                if (descriptionId == 1005) {
                    // gets triggered when Temi's movement is either stopped by pressing specified buttons during a guide transfermovement or in general if you pat temi's head while he's moving
                    robotSpeakText(mRobot, "Ich halte an.", clearQueue = true)
                    tourViewModel.updateGuideState(GuideState.TransferAbort)
                    return
                }

                if (descriptionId == 1006) {
                    // Robot is stuck outside of mapped area.
                    Log.e("Transfer", "Robot is stuck outside of mapped area.")
                    robotSpeakText(
                        mRobot,
                        "Ich kann hier nicht weiterfahren. Bitte schieben Sie mich zurück in den Raum. Oder holen Sie Hilfe",
                        clearQueue = true
                    )
                    tourViewModel.updateGuideState(GuideState.TransferError)
                } else if (tourViewModel.guideState.value == GuideState.TransferGoing || tourViewModel.guideState.value == GuideState.TransferStart) {
                    // Roboter erreicht Ziel nicht
                    robotSpeakText(
                        mRobot,
                        "Hilfe, ich komme hier gerade leider nicht weiter.",
                        clearQueue = true
                    )
                    tourViewModel.updateGuideState(GuideState.TransferError)
                }
            }

            OnGoToLocationStatusChangedListener.REPOSING -> {
                if (tourViewModel.guideState.value == GuideState.TransferGoing) {
                    //Ladespinner anzeigen?
                    robotSpeakText(mRobot, "Einen Moment, ich berechne meine Route.", false)
                }

            }
        }
    }

    override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
        Log.d("SpeakTextListener", "TtsRequest.Status: ${ttsRequest.status}")
        when (ttsRequest.status) {
            TtsRequest.Status.CANCELED,
            TtsRequest.Status.ERROR,
            TtsRequest.Status.COMPLETED -> {
                isSpeaking.value = false
                ttsQueue.value!!.poll()
                ttsQueue.value = ttsQueue.value
            }

            TtsRequest.Status.PROCESSING -> {}
            TtsRequest.Status.STARTED -> {
                isSpeaking.value = true
            }

            else -> {
                Log.w("SpeakTextListener", "Unexpected TtsRequest.Status: ${ttsRequest.status}")
            }
        }
    }

    override fun onBatteryStatusChanged(batteryData: BatteryData?) {
        Log.d("BatteryStatus", "BatteryData: $batteryData")
        if (batteryData != null) {
            tourViewModel.isAtHomeBase.value = batteryData.isCharging
        }
    }

}
