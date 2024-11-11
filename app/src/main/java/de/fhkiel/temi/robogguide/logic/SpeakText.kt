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

fun robotSpeakText(
    mRobot: Robot?,
    text: String?,
    isShowOnConversationLayer: Boolean = false,
    clearQueue: Boolean = false,
    chunk: Boolean = true
) {
    if (clearQueue) {
        clearQueue(mRobot)
    }

    text?.let { txt ->
        if (txt.isEmpty()) {
            Log.d("SpeakText", "Text is empty")
            return
        }

        if (chunk) {
            val chunked = splitTextBySentenceEnd(txt)
            chunked.forEach {
                robotSpeakText(mRobot, it, isShowOnConversationLayer, clearQueue, chunk = false)
            }
            return
        }
        // TODO FIX double speaking
        Log.d("SpeakText", "adding text to queue: $txt")
        mRobot?.let {
            val ttsRequest: TtsRequest = TtsRequest.create(
                speech = txt,
                isShowOnConversationLayer = isShowOnConversationLayer
            )
            // fix needed to make sure that no text duplicates exist in the queue
            ttsQueue.value!!.add(ttsRequest)
            ttsQueue.value = ttsQueue.value
        }
        Log.d("SpeakText", "queue size: ${ttsQueue.value!!.size}")
    }
}

fun clearQueue(mRobot: Robot?) {
    Log.i("SpeakText", "Clearing TTS queue")
    mRobot?.cancelAllTtsRequests()
    ttsQueue.value!!.clear()
    isSpeaking.value = false
    ttsQueue.value = ttsQueue.value
}

fun splitTextBySentenceEnd(text: String): List<String> {
    val sentenceEndRegex = Regex("(?<=[.!?])")
    return text.split(sentenceEndRegex).map { it.trim() }.filter { it.isNotEmpty() }
}