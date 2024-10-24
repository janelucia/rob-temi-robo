package de.fhkiel.temi.robogguide.models

class Place(
    val name: String,
) {
    private val _unimportantLocations: MutableList<Location> = mutableListOf()
    private val _importantLocations: MutableList<Location> = mutableListOf()

}