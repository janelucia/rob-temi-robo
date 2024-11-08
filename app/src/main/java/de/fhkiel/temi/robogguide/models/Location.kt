package de.fhkiel.temi.robogguide.models

/**
 * A Location has multiple items
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
