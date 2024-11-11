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
    if (currentRequest != null && currentRequest.status == TtsRequest.Status.PENDING) {
        mRobot?.speak(currentRequest)
    }
}

fun robotSpeakText(mRobot: Robot?, text: String?, isShowOnConversationLayer: Boolean = false, clearQueue: Boolean = false) {
    if (clearQueue) {
        clearQueue(mRobot)
    }
    //TODO what if the text is bigger than 2000 chars?
    text?.let { txt ->
        if (txt.isEmpty()){
            Log.d("SpeakText", "Text is empty")
            return
        }
        Log.d("SpeakText", "adding text to queue: $txt")
        mRobot?.let {
            val ttsRequest: TtsRequest = TtsRequest.create(
                speech = txt,
                isShowOnConversationLayer = isShowOnConversationLayer
            )
            // fix needed to make sure that no text duplicates exist in the queue
            ttsQueue.value = ttsQueue.value
            ttsQueue.value!!.add(ttsRequest)
            ttsQueue.value = ttsQueue.value
        }
        Log.d("SpeakText", "queue size: ${ttsQueue.value!!.size}")
    }
}

fun clearQueue(mRobot: Robot?) {
    mRobot?.cancelAllTtsRequests()
    ttsQueue.value!!.clear()
    isSpeaking.value = false
    ttsQueue.value = ttsQueue.value
}