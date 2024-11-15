package de.fhkiel.temi.robogguide.models

/**
 * Location: consists of multiple items and resides in a place
 */
class Location(
    val name: String,
    val items: MutableList<Item>,
    val detailedText: Text?,
    val conciseText: Text?
) {
    fun fillYourItemsWithYourself() {
        for (item in items) {
            item.location = this
        }
    }
}
