package de.fhkiel.temi.robogguide.ui.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetupViewModel : ViewModel() {
    private val _isSetupComplete = MutableLiveData(false)
    private val _isRobotReady = MutableLiveData(false)
    val isSetupComplete: LiveData<Boolean> get() = _isSetupComplete
    val isRobotReady: LiveData<Boolean> get() = _isRobotReady

    fun completeSetup() {
        _isSetupComplete.value = true
    }

    fun robotIsReady() {
        _isRobotReady.value = true
    }
}