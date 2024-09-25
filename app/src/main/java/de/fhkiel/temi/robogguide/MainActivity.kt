package de.fhkiel.temi.robogguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnRobotReadyListener
import de.fhkiel.temi.robogguide.database.DynamicDatabaseReader
import java.io.IOException

class MainActivity : AppCompatActivity(), OnRobotReadyListener {
    private var mRobot: Robot? = null
    lateinit var databse: DynamicDatabaseReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // use database
        val databaseName = "roboguide.db"
        databse = DynamicDatabaseReader(this, databaseName)

        try {
            databse.initializeDatabase() // Initialize the database and copy it from assets

            val places = databse.getTableDataAsJson("places") // Fetch data as JSON
            val locations = databse.getTableDataAsJson("locations") // Fetch data as JSON
            Log.i("MainActivity", "Places: $places")
            Log.i("MainActivity", "Locations: $locations")

        } catch (e: IOException) {
            e.printStackTrace()
        }

        // let robot speak on button click
        findViewById<Button>(R.id.btnSpeak).setOnClickListener {
            speak("Hello World!")
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
        databse.closeDatabase()
    }

    override fun onRobotReady(isReady: Boolean) {
        mRobot = if (isReady){
            Robot.getInstance()
        } else {
            null
        }
    }

    private fun speak(text: String){
        mRobot?.let { robot ->
            val ttsRequest: TtsRequest = TtsRequest.create(speech = text, isShowOnConversationLayer = true)
            robot.speak(ttsRequest)
        }
    }

}