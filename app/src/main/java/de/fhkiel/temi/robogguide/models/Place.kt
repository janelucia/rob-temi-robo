package de.fhkiel.temi.robogguide.models

class Place(
    val name: String,
    val importantLocations: List<Location>,
    val allLocations: List<Location>,
    /**
     * All transfers in the place
     * Key: Location_to name
     * Value: Transfer
     */
    val allTransfers: Map<String, Transfer>
)