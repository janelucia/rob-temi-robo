package de.fhkiel.temi.robogguide.models

/**
 * A Location has multiple items
 */
class Location(val name: String) {
    /**
     * A List of all the items for a location.
     */
    private val _items: MutableList<Item> = mutableListOf()

    var detailedText: Text? = null
    var conciseText: Text? = null

    /**
     * Add an item to the location list.
     */
    fun addItem(item: Item) {
       _items.add(item)
    }
}
