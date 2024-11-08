package de.fhkiel.temi.robogguide

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.map.MapDataModel
import com.robotemi.sdk.permission.OnRequestPermissionResultListener
import com.robotemi.sdk.permission.Permission
import de.fhkiel.temi.robogguide.database.DatabaseHelper
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.logic.SetupViewModel
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import de.fhkiel.temi.robogguide.ui.theme.Rob_Temi_Robo_UITheme
import de.fhkiel.temi.robogguide.ui.theme.components.CustomTopAppBar
import de.fhkiel.temi.robogguide.ui.theme.components.GuideNavigationButton
import de.fhkiel.temi.robogguide.ui.theme.pages.Guide
import de.fhkiel.temi.robogguide.ui.theme.pages.GuideExhibition
import de.fhkiel.temi.robogguide.ui.theme.pages.GuideSelector
import de.fhkiel.temi.robogguide.ui.theme.pages.Home
import de.fhkiel.temi.robogguide.ui.theme.pages.Setup
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import de.fhkiel.temi.robogguide.logic.robotSpeakText

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity(), OnRobotReadyListener, OnRequestPermissionResultListener,
    OnUserInteractionChangedListener {
    private val setupViewModel: SetupViewModel by viewModels()
    private val tourViewModel: TourViewModel by viewModels()
    private var mRobot: Robot? = null
    private lateinit var database: DatabaseHelper
    private lateinit var tourManager: TourManager
    private val handler = Handler(Looper.getMainLooper())
    private var isUserInteracting = false
    private lateinit var sharedPreferences: SharedPreferences
    private var dialogShown = false

    private val singleThreadExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val activity: Activity = this

    private val noInteractionRunnable = Runnable {
        if (!isUserInteracting) {
            showInteractionDialog()
        }
    }

    private val returnHomeRunnable = Runnable {
        if (!isUserInteracting) {
            gotoHomeBase()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)


        // ---- DATABASE ACCESS ----
        val databaseName = "roboguide.db"
        database = DatabaseHelper(this, databaseName)

        try {
            database.initializeDatabase() // Initialize the database and copy it from assets
            tourManager = TourManager(database.getDatabase())

            /*
            // EXAMPLE CODE TO ONLY COPY DATABASE TO DIRECTLY USE THE DATABASE FILE FOR ORM
            database.initializeDatabase(withOpen = false)
            val dbFile = database.getDBFile()
            val sqLiteDatabase = database.getDatabase()
            */

            // use json code to get database objects
//            val places = database.getTableDataAsJson("places") // Fetch data as JSON
//            val locations = database.getTableDataAsJson("locations") // Fetch data as JSON
//            Log.i("MainActivity", "Places: $places")
//            Log.i("MainActivity", "Locations: $locations")

        } catch (e: IOException) {
            e.printStackTrace()
            tourManager = TourManager(null) // Initialize with null to simulate error
            tourManager.error.value = e
        }

        setContent {
            val isSetupComplete by setupViewModel.isSetupComplete.observeAsState(false)

            if (isSetupComplete) {
                Rob_Temi_Robo_UITheme {
                    val navController = rememberNavController()
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
                                tourManager,
                                tourViewModel
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
                                    navController,
                                    mRobot,
                                    tourManager,
                                    tourViewModel
                                )
                            }
                            composable("guideExhibition") {
                                GuideExhibition(
                                    innerPadding,
                                    mRobot,
                                    tourManager
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
        Robot.getInstance().addOnRobotReadyListener(this)
        Robot.getInstance().addOnRequestPermissionResultListener(this)
        Robot.getInstance().addOnUserInteractionChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        Robot.getInstance().removeOnRobotReadyListener(this)
        Robot.getInstance().removeOnRequestPermissionResultListener(this)
        Robot.getInstance().removeOnUserInteractionChangedListener(this)
        // disable the detectionMode again
        mRobot?.setDetectionModeOn(on = false, distance = 0.8f)
        mRobot?.setKioskModeOn(on = false)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.closeDatabase()
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {

            Log.i("MainActivity", "Robot is ready")
            mRobot = Robot.getInstance()

            // request to be kiosk app


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

            // hide pull-down bar
            val activityInfo: ActivityInfo =
                packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            Robot.getInstance().onStart(activityInfo)

            setupViewModel.robotIsReady()
            // showMapData()
        }
    }

    /**
     * Speaks a text using the tmi tts
     * @param text                          [String] text to speak
     * @param isShowOnConversationLayer     [Boolean] true (default) to show conversation layer while speaking, false to hide it.
     */
    private fun speakText(text: String, isShowOnConversationLayer: Boolean = true) {
        mRobot.let { robot ->
            val ttsRequest: TtsRequest = TtsRequest.create(
                speech = text,
                isShowOnConversationLayer = isShowOnConversationLayer
            )
            robot?.speak(ttsRequest)
        }
    }

    /**
     * Uses temi tts to speak every listed location on the active map
     */
    private fun speakLocations() {
        mRobot.let { robot ->
            var text = "Das sind alle Orte an die ich gehen kann:"
            robot?.locations?.forEach {
                text += " $it,"
            }
            speakText(text, isShowOnConversationLayer = false)
        }
    }

    /**
     * Uses temi sdk function to go to home base
     */
    private fun gotoHomeBase() {
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
        if (mRobot?.checkSelfPermission(permission) == Permission.GRANTED) {
            return false
        } else {
            mRobot?.requestPermissions(listOf(permission), requestCode)
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
            "PERMISSION RESULT",
            "permission $permission with request code $requestCode with result = $grantResult"
        )

        when (permission) {
            Permission.MAP -> when (requestCode) {
                REQUEST_CODE_MAP -> {
                    showMapData()
                }
            }

            // Permission.FACE_RECOGNITION -> TODO
            // Permission.SETTINGS -> TODO
            // Permission.UNKNOWN -> TODO

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
            handler.postDelayed(noInteractionRunnable, 5000) // 5 minutes
        }
    }

    private fun showInteractionDialog() {
        Log.i("MainActivity", "Show interaction dialog")
        Log.d("MainActivity", "User is not interacting, showing dialog $dialogShown")

        assert(mRobot != null)
        val oldVolume = mRobot?.volume!!

        if (!dialogShown) {
            dialogShown = true
            // scream
            mRobot?.volume = 100
            robotSpeakText(mRobot, "Hallo, werde ich noch gebraucht?", false)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Tour fortführen?")
                .setMessage("Ich habe längere Zeit keine Interaktion festgestellt.")
                .setMessage("Möchtest du die Tour fortsetzen oder darf der Roboter zurück zur Homebase?")
                .setPositiveButton("Tour fortführen") { _, _ ->
                    isUserInteracting = true
                    handler.removeCallbacks(returnHomeRunnable)
                    mRobot?.volume = oldVolume
                }
                .setNegativeButton("Tour beenden und Roboter zur Homebase schicken") { _, _ ->
                    gotoHomeBase()
                    mRobot?.volume = oldVolume
                }
                .show()
        }
        // TODO timer starten und ihn nach Hause schicken.. nicht vergessen Lautstärke zurückzusetzen
        // mRobot?.volume = oldVolume
        // TODO ggf. den COuntdown timer in dem Dialog anzeigen lassen?
    }

    companion object {
        const val REQUEST_CODE_MAP = 10
    }

}
