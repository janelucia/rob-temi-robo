package de.fhkiel.temi.robogguide.ui.logic

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.Location

class TourViewModel : ViewModel() {
    var currentLocation by mutableIntStateOf(0)
    var tourLocations: List<Location> = emptyList()
    var numberOfLocations: Int = tourLocations.size

    var currentItem by mutableIntStateOf(0)
    var currentLocationItems: List<Item> = emptyList()
    var numberOfItemsAtCurrentLocation: Int = currentLocationItems.size


    fun fillTourLocations(locations: List<Location>) {
        tourLocations = locations
        numberOfLocations = tourLocations.size
    }

    fun fillLocationItems(items: List<Item>) {
        currentLocationItems = items
        numberOfItemsAtCurrentLocation = currentLocationItems.size
    }

    fun updateCurrentLocation(index: Int) {
        if (index in 0 until numberOfLocations) {
            currentLocation = index
        }
    }

    fun updateCurrentItem(index: Int) {
        if (index in 0 until numberOfItemsAtCurrentLocation) {
            currentItem = index
        }
    }
}

