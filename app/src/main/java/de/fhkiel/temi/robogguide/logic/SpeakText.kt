package de.fhkiel.temi.robogguide.logic

import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import java.util.LinkedList
import java.util.Queue

val ttsQueue: Queue<TtsRequest> = LinkedList()
var isSpeaking: Boolean = false



private fun processQueue(mRobot: Robot?) {
    if (!isSpeaking && ttsQueue.isNotEmpty()) {
        val currentRequest = ttsQueue.poll()
        if (currentRequest != null) {
            isSpeaking = true
            mRobot?.speak(currentRequest)
        }
    }
}

fun robotSpeakText(mRobot: Robot?, text: String?, isShowOnConversationLayer: Boolean = true) {

    text?.let { txt ->
        mRobot?.let { robot ->
            val ttsRequest: TtsRequest = TtsRequest.create(
                speech = txt,
                isShowOnConversationLayer = isShowOnConversationLayer
            )
            ttsQueue.add(ttsRequest)
            processQueue(mRobot)
        }

    }
}