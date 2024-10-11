// Datei: app/src/main/java/com/example/rob_temi_robo_ui/MainActivity.kt
package de.fhkiel.temi.robogguide

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import de.fhkiel.temi.robogguide.database.DynamicDatabaseReader
import de.fhkiel.temi.robogguide.ui.theme.Rob_Temi_Robo_UITheme
import de.fhkiel.temi.robogguide.ui.theme.components.CustomTopAppBar
import de.fhkiel.temi.robogguide.ui.theme.pages.Guide
import de.fhkiel.temi.robogguide.ui.theme.pages.GuideSelector
import de.fhkiel.temi.robogguide.ui.theme.pages.Home
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity(), OnRobotReadyListener {

    private var mRobot: Robot? = null
    private lateinit var database: DynamicDatabaseReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // use database
        val databaseName = "roboguide.db"
        database = DynamicDatabaseReader(this, databaseName)

        try {
            database.initializeDatabase() // Initialize the database and copy it from assets

            val places = database.getTableDataAsJson("places") // Fetch data as JSON
            val locations = database.getTableDataAsJson("locations") // Fetch data as JSON
            Log.i("MainActivity", "Places: $places")
            Log.i("MainActivity", "Locations: $locations")

        } catch (e: IOException) {
            e.printStackTrace()
        }

        // let robot speak on button click
        findViewById<Button>(R.id.btnSpeakHelloWorld).setOnClickListener {
            speakHelloWorld("Hello World!")
        }

        findViewById<Button>(R.id.btnSpeakLocations).setOnClickListener {
            speakLocations()
        }

        findViewById<Button>(R.id.btnCancelSpeak).setOnClickListener {
            mRobot?.cancelAllTtsRequests()
        }

        findViewById<Button>(R.id.btnGotoHomeBase).setOnClickListener {
            gotoHomeBase()
        }

        findViewById<Button>(R.id.btnExitApp).setOnClickListener {
            finishAffinity()
        }

        setContent {
            Rob_Temi_Robo_UITheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { CustomTopAppBar(navController) }
                ) { innerPadding ->
                    NavHost(navController, startDestination = "homePage") {
                        composable("homePage") { Home(innerPadding, navController) }
                        composable("guideSelector") { GuideSelector(innerPadding, navController) }
                        composable("guide") { Guide(innerPadding, navController) }
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Robot.getInstance().addOnRobotReadyListener(this)
    }

    override fun onStop() {
        super.onStop()
        Robot.getInstance().removeOnRobotReadyListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.closeDatabase()
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady){
            mRobot = Robot.getInstance()
            mRobot?.hideTopBar()        // hide top action bar

            // hide pull-down bar
            val activityInfo: ActivityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            Robot.getInstance().onStart(activityInfo)

        }
    }

    private fun speakHelloWorld(text: String, isShowOnConversationLayer: Boolean = true){
        mRobot?.let { robot ->
            val ttsRequest: TtsRequest = TtsRequest.create(speech = text, isShowOnConversationLayer = isShowOnConversationLayer)
            robot.speak(ttsRequest)
        }
    }

    private fun speakLocations(){
        mRobot?.let { robot ->
            var text = "Das sind alle Orte an die ich gehen kann:"
            robot.locations.forEach {
                text += " $it,"
            }
            speakHelloWorld(text, isShowOnConversationLayer = false)
        }
    }

    private fun gotoHomeBase(){
        mRobot?.goTo(location = "home base")
    }
}
