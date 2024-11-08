package de.fhkiel.temi.robogguide.ui.logic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.fhkiel.temi.robogguide.models.GuideState
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.LevelOfDetail
import de.fhkiel.temi.robogguide.models.Location

class TourViewModel : ViewModel() {
    var currentLocationIndex = MutableLiveData(0)
    var tourLocations: MutableList<Location> = mutableListOf()
    var tourLocationsAsItems: MutableList<Item> = mutableListOf()
    var numberOfLocations: Int = tourLocations.size
    var levelOfDetail: LevelOfDetail? = null

    private val _currentLocationItems: MutableList<Item> = mutableListOf()

    val currentItemIndex = MutableLiveData(0)
    val numberOfItemsAtCurrentLocation = MutableLiveData(_currentLocationItems.size)

    val guideState = MutableLiveData<GuideState>()
    val currentItem = MutableLiveData<Item>()
    val currentLocation = MutableLiveData<Location>()
    val wasAlreadySpoken = MutableLiveData(false)


    /* Initial funssssssssss */
    fun fillTourLocations(locations: MutableList<Location>) {
        tourLocations.clear()
        tourLocations.addAll(locations)
        //TODO tourTransferTexts befüllen
        numberOfLocations = tourLocations.size
        tourLocationsAsItems = createListOfLocationsAsItems()

        currentLocationIndex.value = 0
        currentLocation.value = tourLocations[currentLocationIndex.value!!]

        fillLocationItems(currentLocation.value!!.items)
        guideState.value = GuideState.TransferStart
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
        _currentLocationItems.add(0, tourLocationsAsItems[currentLocationIndex.value!!])

        numberOfItemsAtCurrentLocation.value = _currentLocationItems.size
        currentItemIndex.value = 0
        currentItem.value = _currentLocationItems[0]
    }


    /* Navigation */

    private fun updateCurrentLocation(index: Int) {
        if (index in 0 until numberOfLocations) {
            currentLocationIndex.value = index
            currentLocation.value = tourLocations[currentLocationIndex.value!!]

            fillLocationItems(tourLocations[currentLocationIndex.value!!].items)
            //trigger new transfer navigation
            guideState.value = GuideState.TransferStart
        } else if (index > numberOfLocations) {
            guideState.value = GuideState.End
            // Tour Endescreen aufrufen?
        }
    }

    private fun incrementCurrentLocationIndex() {
        updateCurrentLocation(currentLocationIndex.value!! + 1)
    }

    private fun decrementCurrentLocationIndex() {
        updateCurrentLocation(currentLocationIndex.value!! - 1)
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
            incrementCurrentLocationIndex()
            currentItemIndex.value = 0
            currentItem.value = _currentLocationItems[0]
        } else if (index < 0) {
            decrementCurrentLocationIndex()
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
        return tourLocations[currentLocationIndex.value!!]
    }
}
