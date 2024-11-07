package de.fhkiel.temi.robogguide.ui.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.fhkiel.temi.robogguide.models.GuideState
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.LevelOfDetail
import de.fhkiel.temi.robogguide.models.Location

class TourViewModel : ViewModel() {
    var currentLocationIndex by mutableIntStateOf(0)
    var tourLocations: MutableList<Location> = mutableListOf()
    var tourLocationsAsItems: MutableList<Item> = mutableListOf()
    var numberOfLocations: Int = tourLocations.size
    var levelOfDetail: LevelOfDetail? = null

    private val _currentLocationItems: MutableList<Item> = mutableListOf()

    val currentItemIndex = MutableLiveData(0)
    val numberOfItemsAtCurrentLocation = MutableLiveData(_currentLocationItems.size)

    val guideState = MutableLiveData<GuideState>()
    val currentItem = MutableLiveData<Item>()
    val wasAlreadySpoken = MutableLiveData(false)


    /* Initial funssssssssss */
    fun fillTourLocations(locations: MutableList<Location>) {
        tourLocations.clear()
        tourLocations.addAll(locations)
        numberOfLocations = tourLocations.size
        tourLocationsAsItems = createListOfLocationsAsItems()

        currentLocationIndex = 0
        fillLocationItems(tourLocations[currentLocationIndex].items)
    }

    private fun createListOfLocationsAsItems(): MutableList<Item> {
        val locationInfosAsItems: MutableList<Item> = mutableListOf()
        for (location in tourLocations) {
            locationInfosAsItems.add(makeItemFromLocationInfo(location))
        }
        return locationInfosAsItems
    }

    private fun makeItemFromLocationInfo(location: Location): Item {
        val curLocationAsItem = Item(
            location.name,
            location.detailedText,
            location.conciseText
        )
        return curLocationAsItem
    }

    /* Items */

    private fun fillLocationItems(items: MutableList<Item>) {
        _currentLocationItems.clear()
        _currentLocationItems.addAll(items)
        _currentLocationItems.add(0, tourLocationsAsItems[currentLocationIndex])

        numberOfItemsAtCurrentLocation.value = _currentLocationItems.size
        currentItemIndex.value = 0
        currentItem.value = _currentLocationItems[0]
    }


    /* Navigation */

    fun updateCurrentLocation(index: Int) {
        if (index in 0 until numberOfLocations) {
            currentLocationIndex = index
            fillLocationItems(tourLocations[currentLocationIndex].items)
        }
    }

    fun incrementCurrentItemIndex() {
        updateCurrentItem(currentItemIndex.value!! + 1)
    }

    fun decrementCurrentItemIndex() {
        updateCurrentItem(currentItemIndex.value!! - 1)
    }

    fun updateCurrentItem(index: Int) {
        if (index in 0 until numberOfItemsAtCurrentLocation.value!!) {
            currentItemIndex.value = index
            currentItem.value = _currentLocationItems[index]
        } else if (index >= numberOfItemsAtCurrentLocation.value!!) {
            updateCurrentLocation(currentLocationIndex + 1)
            currentItemIndex.value = 0
            currentItem.value = _currentLocationItems[0]
        } else if (index < 0) {
            updateCurrentLocation(currentLocationIndex - 1)
            currentItemIndex.value = 0
            currentItem.value = _currentLocationItems[0]
        }
    }

    fun updateGuideState(newState: GuideState) {
        guideState.value = newState
    }

    fun updateAlreadySpoken(alreadySpoken: Boolean) {
        wasAlreadySpoken.value = alreadySpoken
    }

    /* Access for media */

    fun giveCurrentLocation(): Location {
        return tourLocations[currentLocationIndex]
    }
}
