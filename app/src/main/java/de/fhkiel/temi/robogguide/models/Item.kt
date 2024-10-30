package de.fhkiel.temi.robogguide.models

/**
 * A item is a specific exhibit located at a location.
 */
class Item(val name: String) {
    var detailedText: Text? = null
    var conciseText: Text? = null
}