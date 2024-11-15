package de.fhkiel.temi.robogguide.models

/**
 * Place: represents a place which consists of locations
 */
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