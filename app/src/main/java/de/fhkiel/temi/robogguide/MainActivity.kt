package de.fhkiel.temi.robogguide

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import de.fhkiel.temi.robogguide.ui.theme.Rob_Temi_Robo_UITheme
import de.fhkiel.temi.robogguide.ui.theme.components.CustomTopAppBar
import de.fhkiel.temi.robogguide.ui.theme.pages.Guide
import de.fhkiel.temi.robogguide.ui.theme.pages.GuideSelector
import de.fhkiel.temi.robogguide.ui.theme.pages.Home
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity(), OnRobotReadyListener, OnRequestPermissionResultListener {
    private var mRobot: Robot? = null
    private lateinit var database: DatabaseHelper
    private lateinit var tourManager: TourManager

    private val singleThreadExecutor: ExecutorService = Executors.newSingleThreadExecutor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ---- DATABASE ACCESS ----
        val databaseName = "roboguide.db"
        database = DatabaseHelper(this, databaseName)

        try {
            database.initializeDatabase() // Initialize the database and copy it from assets
            tourManager = TourManager(database)

            /*
            // EXAMPLE CODE TO ONLY COPY DATABASE TO DIRECTLY USE THE DATABASE FILE FOR ORM
            database.initializeDatabase(withOpen = false)
            val dbFile = database.getDBFile()
            val sqLiteDatabase = database.getDatabase()
            */

            // use json code to get database objects
            val places = database.getTableDataAsJson("places") // Fetch data as JSON
            val locations = database.getTableDataAsJson("locations") // Fetch data as JSON
            Log.i("MainActivity", "Places: $places")
            Log.i("MainActivity", "Locations: $locations")

        } catch (e: IOException) {
            e.printStackTrace()
        }

        setContent {
            Rob_Temi_Robo_UITheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { CustomTopAppBar(navController) }
                ) { innerPadding ->
                    NavHost(navController, startDestination = "homePage") {
                        composable("homePage") { Home(innerPadding, navController, mRobot) }
                        composable("guideSelector") { GuideSelector(innerPadding, navController, mRobot) }
                        composable("guide") { Guide(innerPadding, navController, mRobot) }
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Robot.getInstance().addOnRobotReadyListener(this)
        Robot.getInstance().addOnRequestPermissionResultListener(this)
    }

    override fun onStop() {
        super.onStop()
        Robot.getInstance().removeOnRobotReadyListener(this)
        Robot.getInstance().removeOnRequestPermissionResultListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.closeDatabase()
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady){
            mRobot = Robot.getInstance()

            // ---- DISABLE TEMI UI ELEMENTS ---
            mRobot?.hideTopBar()        // hide top action bar

            // hide pull-down bar
            val activityInfo: ActivityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            Robot.getInstance().onStart(activityInfo)

            showMapData()
        }
    }

    /**
     * Speaks a text using the tmi tts
     * @param text                          [String] text to speak
     * @param isShowOnConversationLayer     [Boolean] true (default) to show conversation layer while speaking, false to hide it.
     */
    private fun speakText(text: String, isShowOnConversationLayer: Boolean = true){
        mRobot.let { robot ->
            val ttsRequest: TtsRequest = TtsRequest.create(speech = text, isShowOnConversationLayer = isShowOnConversationLayer)
            robot?.speak(ttsRequest)
        }
    }

    /**
     * Uses temi tts to speak every listed location on the active map
     */
    private fun speakLocations(){
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
    private fun gotoHomeBase(){
        mRobot?.goTo(location = "home base")
    }


    /**
     * Gets the [MapDataModel] of the robot and shows its data in Logcat
     */
    private fun showMapData(){
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

        Log.i( "Map-List", "${mRobot?.getMapList()}")
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
            return false;
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
        Log.d("PERMISSION RESULT", "permission $permission with request code $requestCode with result = $grantResult")

        when (permission){
            Permission.MAP -> when (requestCode){
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

    companion object{
        const val REQUEST_CODE_MAP = 10
    }

}
