package de.fhkiel.temi.robogguide.ui.logic

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class TourViewModel : ViewModel() {
    private var currentExhibit by mutableIntStateOf(0)

    fun setCurrentExhibit(index: Int) {
        currentExhibit = index
    }
}

