package de.fhkiel.temi.robogguide.logic

import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import kotlinx.coroutines.delay

var isSpeaking = false

fun robotSpeakText(mRobot: Robot?, text: String?, isShowOnConversationLayer: Boolean = true) {

    if (isSpeaking) {
        return
    }

    text?.let { txt ->
        mRobot?.let { robot ->
            val ttsRequest: TtsRequest = TtsRequest.create(
                speech = txt,
                isShowOnConversationLayer = isShowOnConversationLayer
            )
            isSpeaking = true
            robot.speak(ttsRequest)
        }

    }
}