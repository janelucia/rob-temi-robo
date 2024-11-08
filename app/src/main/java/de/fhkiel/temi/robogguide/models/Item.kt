package de.fhkiel.temi.robogguide.models

/**
 * A item is a specific exhibit located at a location.
 */
class Item(
    val name: String,
    val detailedText: Text?,
    val conciseText: Text?,
    var location: Location? = null
)