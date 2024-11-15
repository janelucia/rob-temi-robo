package de.fhkiel.temi.robogguide.models

/**
 * Item: a specific exhibit located at a location.
 */
class Item(
    val name: String,
    val detailedText: Text?,
    val conciseText: Text?,
    var location: Location? = null
)