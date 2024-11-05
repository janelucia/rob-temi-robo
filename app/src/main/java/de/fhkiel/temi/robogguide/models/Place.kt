package de.fhkiel.temi.robogguide.models

class Place(
    val name: String,
    val unimportandLocations: List<Location>,
    val importantLocations: List<Location>,
    val allLocations: List<Location>
)