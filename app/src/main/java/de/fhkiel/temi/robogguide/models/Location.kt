package de.fhkiel.temi.robogguide.models

/**
 * A Location has multiple items
 */
class Location(
    val name: String,
    val items: List<Item>,
    val detailedText: Text?,
    val conciseText: Text?
)
