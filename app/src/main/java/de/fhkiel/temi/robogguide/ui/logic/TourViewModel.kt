package de.fhkiel.temi.robogguide.ui.logic

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import de.fhkiel.temi.robogguide.models.Location

class TourViewModel : ViewModel() {
    var currentExhibit by mutableIntStateOf(0)
    var tourLocations: List<Location> = emptyList()
    var numberOfExhibits: Int = tourLocations.size

    fun fillTourLocations(locations: List<Location>) {
        tourLocations = locations
        numberOfExhibits = tourLocations.size
    }

    fun updateCurrentExhibit(index: Int) {
        if (index in 0 until numberOfExhibits) {
            currentExhibit = index
        }
    }
}

