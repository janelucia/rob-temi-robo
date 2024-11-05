package de.fhkiel.temi.robogguide.ui.logic

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import de.fhkiel.temi.robogguide.models.Location

class TourViewModel : ViewModel() {
    private var currentExhibit by mutableIntStateOf(0)
    private var tourLocations: List<Location> = emptyList()

    val numberOfExhibits: Int = tourLocations.size

    fun setTourLocations(locations: List<Location>) {
        tourLocations = locations
    }

    fun getTourLocations(): List<Location> {
        return tourLocations
    }

    fun setCurrentExhibit(index: Int) {
        if (index in 0 until numberOfExhibits) {
            currentExhibit = index
        }
    }

    fun getCurrentExhibit(): Int {
        return currentExhibit
    }
}

