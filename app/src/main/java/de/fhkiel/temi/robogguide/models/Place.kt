package de.fhkiel.temi.robogguide.models

class Place(
    val name: String,
) {
    /**
     * A list of unimportant locations, that could be skipped if needed.
     */
    private val _unimportantLocations: MutableList<Location> = mutableListOf()

    /**
     * A list of important locations. That should definitely be shown.
     */
    private val _importantLocations: MutableList<Location> = mutableListOf()

    /**
     * Add a location either in the important or unimportant list.
     */
    fun addLocation(location: Location, isImportant: Boolean) {
        if (isImportant) {
            _importantLocations.add(location)
        } else {
            _unimportantLocations.add(location)
        }
    }


}