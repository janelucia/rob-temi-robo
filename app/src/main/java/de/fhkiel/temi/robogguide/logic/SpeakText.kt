package de.fhkiel.temi.robogguide.logic

import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import kotlinx.coroutines.delay


fun robotSpeakText(mRobot: Robot?, text: String?, isShowOnConversationLayer: Boolean = true) {

    text?.let { txt ->
        mRobot?.let { robot ->
            val ttsRequest: TtsRequest = TtsRequest.create(
                speech = txt,
                isShowOnConversationLayer = isShowOnConversationLayer
            )
            robot.speak(ttsRequest)
        }

    }
}