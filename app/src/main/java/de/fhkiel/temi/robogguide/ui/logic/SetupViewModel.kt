package de.fhkiel.temi.robogguide.ui.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetupViewModel : ViewModel() {
    private val _isSetupComplete = MutableLiveData(false)
    val isSetupComplete: LiveData<Boolean> get() = _isSetupComplete

    fun completeSetup() {
        _isSetupComplete.value = true
    }
}