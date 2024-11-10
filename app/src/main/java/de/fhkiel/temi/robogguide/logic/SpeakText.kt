package de.fhkiel.temi.robogguide.logic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import java.util.LinkedList
import java.util.Queue

val ttsQueue = MutableLiveData<Queue<TtsRequest>>(LinkedList())
var isSpeaking = MutableLiveData(false)

fun processQueue(mRobot: Robot?) {
    val currentRequest = ttsQueue.value!!.first()
    if (currentRequest != null) {
        mRobot?.speak(currentRequest)
    }
}

fun robotSpeakText(mRobot: Robot?, text: String?, isShowOnConversationLayer: Boolean = false, clearQueue: Boolean = false) {
    if (clearQueue) {
        clearQueue(mRobot)
    }
    text?.let { txt ->
        Log.d("SpeakText", "added text to queue")
        mRobot?.let {
            val ttsRequest: TtsRequest = TtsRequest.create(
                speech = txt,
                isShowOnConversationLayer = isShowOnConversationLayer
            )
            ttsQueue.value!!.add(ttsRequest)
            ttsQueue.value = ttsQueue.value
        }
        Log.d("SpeakText", "queue size: ${ttsQueue.value!!.size}")
    }
}

fun clearQueue(mRobot: Robot?) {
    mRobot?.cancelAllTtsRequests()
    ttsQueue.value!!.clear()
    ttsQueue.value = ttsQueue.value
}