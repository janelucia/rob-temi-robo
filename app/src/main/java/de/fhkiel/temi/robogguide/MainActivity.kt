package de.fhkiel.temi.robogguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnRobotReadyListener

class MainActivity : AppCompatActivity(), OnRobotReadyListener {
    private var mRobot: Robot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    override fun onRobotReady(isReady: Boolean) {
        if (isReady){
            mRobot = Robot.getInstance()
        } else {
            mRobot = null
        }
    }

    fun speak(text: String){
        mRobot?.let { robot ->
            val ttsRequest: TtsRequest = TtsRequest.create(speech = text, isShowOnConversationLayer = true)
            robot.speak(ttsRequest)
        }
    }

}