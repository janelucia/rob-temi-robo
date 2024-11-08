package de.fhkiel.temi.robogguide.ui.logic

import android.content.Context
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SetupViewModel(application: Application) : AndroidViewModel(application) {
    private val _isSetupComplete = MutableLiveData(false)
    private val _isRobotReady = MutableLiveData(false)
    val isSetupComplete: LiveData<Boolean> get() = _isSetupComplete
    val isRobotReady: LiveData<Boolean> get() = _isRobotReady

    private val sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _isKioskModeEnabled = MutableLiveData(sharedPreferences.getBoolean("kiosk_mode", false))
    val isKioskModeEnabled: LiveData<Boolean> get() = _isKioskModeEnabled

    fun setKioskModeEnabled(enabled: Boolean) {
        _isKioskModeEnabled.value = enabled
        sharedPreferences.edit().putBoolean("kiosk_mode", enabled).apply()
    }

    fun completeSetup() {
        _isSetupComplete.value = true
    }

    fun robotIsReady() {
        _isRobotReady.value = true
    }
}