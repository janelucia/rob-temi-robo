package de.fhkiel.temi.robogguide.logic

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.LevelOfDetail
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.models.Media
import de.fhkiel.temi.robogguide.models.Place
import de.fhkiel.temi.robogguide.models.Text
import java.net.URL

class TourManager(private val db: SQLiteDatabase?) {

    private var currentPlaceId: Int? = null
    private var currentPlaceName: String? = null
    val allPlacesMap: MutableMap<Int, String> = mutableMapOf()
    var error: Exception? = null

    var selectedPlace: Place? = null
    var selectedLevelOfDetail: LevelOfDetail? = null

    init {
        // try catch to handle an error like a wrongly named database
        try {
            checkDatabase()
            parseTours()
        } catch (e: Exception) {
            error = e
            Log.e("TourManager", "Error initializing TourManager: ${e.message}")
        }
    }

    /**
     * Method to parse the database and create [Tour] instances.
     */
    private fun parseTours() {
        Log.i("TourManager", "Parsing tours")

    }

    /**
     * Method to check the database for validity.
     */
    private fun checkDatabase() {

        val locationIds = mutableSetOf<Int>()
        val startLocations = mutableSetOf<Int>()
        val endLocations = mutableSetOf<Int>()
        val fromCount = mutableMapOf<Int, Int>()
        val toCount = mutableMapOf<Int, Int>()
        val tourStartLocation = mutableSetOf<Int>()
        val tourEndLocation = mutableSetOf<Int>()
        val errorMessage =
            "Die Datenbank scheint nicht korrekt befüllt zu sein.\nFolgender Fehler ist aufgetreten:\n"

        Log.i("TourManager", "Checking database for validity")

        if (db == null) {
            Log.e("TourManager", "Database is not initialized")
            throw IllegalStateException(errorMessage + "Datenbank ist nicht initialisiert.")
        }

        val places = db.rawQuery("SELECT * FROM places", null)
        val locations = db.rawQuery("SELECT * FROM locations", null)
        val transfers = db.rawQuery(
            """
            SELECT t.*, p1.id as P1_ID, p2.id as P2_ID FROM transfers AS t
            LEFT JOIN locations AS l1 
            ON t.location_from = l1.id 
            LEFT JOIN locations AS l2
            ON t.location_to = l2.id 
            LEFT JOIN places AS p1
            ON l1.places_id = p1.id 
            LEFT JOIN places as p2
            ON l2.places_id = p2.id""", null
        )
        val items = db.rawQuery("SELECT * FROM items", null)
        val texts = db.rawQuery("SELECT * FROM texts", null)

        places.use {
            if (it.count == 0) {
                Log.e("TourManager", "Places is empty")
                throw IllegalStateException(errorMessage + "Es sind keine Karten (Places) vorhanden.")
            }
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val name = it.getString(it.getColumnIndexOrThrow("name"))
                    Log.i("TourManager", "Place: $name")
                    allPlacesMap[id] = name
                } while (it.moveToNext())
            }
        }

        locations.use {
            if (it.count == 0) {
                Log.e("TourManager", "Locations is empty")
                throw IllegalStateException(errorMessage + "Es sind keine Orte (Locations) vorhanden.")
            }
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val placesId = it.getInt(it.getColumnIndexOrThrow("places_id"))

                    locationIds.add(id)
                    Log.i("TourManager", "Location ID: $id")

                    if (!allPlacesMap.keys.contains(placesId)) {
                        Log.e("TourManager", "Location with unknown Place found")
                        throw IllegalStateException(errorMessage + "Die Location mit der ID: $id wurde einem Ort mit der \"places_id\": $placesId zugeordnet. Dieser Ort konnte nicht in der Datenbank gefunden werden!")
                    }

                } while (it.moveToNext())
            }
        }

        items.use {
            if (it.count == 0) {
                Log.e("TourManager", "Items is empty")
                throw IllegalStateException(errorMessage + "Es sind keine Ausstellungsstücke vorhanden.")
            }
            if (it.moveToFirst()) {
                do {
                    val locationId = it.getInt(it.getColumnIndexOrThrow("locations_id"))
                    if (!locationIds.contains(locationId)) {
                        Log.e("TourManager", "Invalid location ID in items")
                        throw IllegalStateException(errorMessage + "Ungültige Orts-ID in der Items-Tabelle.")
                    }
                    Log.i("TourManager", "Item at location ID: $locationId")
                } while (it.moveToNext())
            }
        }

        transfers.use {
            if (it.count == 0) {
                Log.e("TourManager", "Transfers is empty")
                throw IllegalStateException(errorMessage + "Es sind keine Verbindungen (Transfers) zwischen den Orten vorhanden.")
            }
            if (it.moveToFirst()) {
                do {
                    val from = it.getInt(it.getColumnIndexOrThrow("location_from"))
                    val to = it.getInt(it.getColumnIndexOrThrow("location_to"))
                    val p1 = it.getInt(it.getColumnIndexOrThrow("P1_ID"))
                    val p2 = it.getInt(it.getColumnIndexOrThrow("P2_ID"))

                    if (p1 != p2 && p1 != 0 && p2 != 0) {
                        Log.e("TourManager", "Error in Transfer: $from -> $to")
                        throw IllegalStateException(errorMessage + "Die Transfers scheinen zwischen mehreren Places zu laufen. $from -> $to")
                    }

                    if ((!locationIds.contains(from) || !locationIds.contains(to)) && to != -1 && from != -1) {
                        Log.e("TourManager", "Invalid transfer: $from -> $to")
                        throw IllegalStateException(errorMessage + "Ungültige Verbindung (Transfer) zwischen den Orten: $from -> $to")
                    }

                    if (to != -1) {
                        fromCount[from] = fromCount.getOrDefault(from, 0) + 1
                    } else {
                        if (!tourStartLocation.add(p1)) {
                            Log.e("TourManager", "Mutliple Tour Start Locations detected")
                            throw IllegalStateException(errorMessage + "Mehrere Startorte für eine Tour gefunden.")
                        }
                    }
                    if (from != -1) {
                        toCount[to] = toCount.getOrDefault(to, 0) + 1
                    } else {
                        if (!tourEndLocation.add(p2)) {
                            Log.e("TourManager", "Mutliple Tour End Locations detected")
                            throw IllegalStateException(errorMessage + "Mehrere Zielorte für eine Tour gefunden.")
                        }
                    }
                    startLocations.add(from)
                    endLocations.add(to)
                    Log.i("TourManager", "Transfer: $from -> $to")
                } while (it.moveToNext())
            }
        }

        texts.use {
            if (it.count == 0) {
                Log.e("TourManager", "Texts is empty")
                throw IllegalStateException(errorMessage + "Es sind keine Texte vorhanden.")
            }
            if (it.moveToFirst()) {
                do {
                    // check that at least one id is set: locations_id, items_id, transfers_id
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val locationId = it.getInt(it.getColumnIndexOrThrow("locations_id"))
                    val itemId: Int = it.getInt(it.getColumnIndexOrThrow("items_id"))
                    val transferId = it.getInt(it.getColumnIndexOrThrow("transfers_id"))

                    if (locationId == 0 && itemId == 0 && transferId == 0) {
                        Log.e("TourManager", "No ID set in text entry $id")
                        throw IllegalStateException(errorMessage + "Der Text mit der ID $id hat keine gültige ID Zuweisung.")
                    }

                    Log.i("TourManager", "Text")
                } while (it.moveToNext())
            }
        }

        fromCount.forEach { (id, count) ->
            if (count > 1) {
                Log.e("TourManager", "ID $id appears more than once in the 'from' column")
                throw IllegalStateException(errorMessage + "ID $id erscheint mehr als einmal in der 'from' Spalte von der Verbindungstabelle (transfers).")
            }
        }

        toCount.forEach { (id, count) ->
            if (count > 1) {
                Log.e("TourManager", "ID $id appears more than once in the 'to' column")
                throw IllegalStateException(errorMessage + "ID $id erscheint mehr als einmal in der 'to' Spalte von der Verbindungstabelle (transfers).")
            }
        }

        val startLocation = startLocations.subtract(endLocations)
        val endLocation = endLocations.subtract(startLocations)

        if (startLocation.size != 1 || endLocation.size != 1) {
            Log.e("TourManager", "Invalid number of start or end locations")
            throw IllegalStateException(errorMessage + "Ungültige Anzahl von Start- oder Endorten.")
        }

        Log.i("TourManager", "Start location: ${startLocation.first()}")
        Log.i("TourManager", "End location: ${endLocation.first()}")

        // Check for the start location again to stop as it indicates another tour
        db.rawQuery("SELECT * from transfers", null).use { newTransfers ->
            if (newTransfers.moveToFirst()) {
                do {
                    val from =
                        newTransfers.getInt(newTransfers.getColumnIndexOrThrow("location_from"))
                    if (from == startLocation.first()) {
                        Log.i("TourManager", "Encountered start location again, stopping check")
                        break
                    }
                } while (newTransfers.moveToNext())
            }
        }

        Log.i("TourManager", "Database is valid")
    }


    fun setPlace(placeId: Int, placeName: String) {
        Log.i("TourManager", "Set current place to $placeName")
        currentPlaceId = placeId
        currentPlaceName = placeName
        fillThePlaceWithData()
    }

    private fun fillThePlaceWithData() {
        Log.i("TourManager", "Filling the selected place with data")

        val (allLocations, importantLocations) = getLocations()
        selectedPlace = Place(currentPlaceName!!, importantLocations, allLocations)
    }

    /**
     * Method to get all locations for the current place.
     */
    private fun getLocations(): Pair<List<Location>, List<Location>> {

        val transfersForLocation: MutableMap<Int, Int> = mutableMapOf()
        var startPoint = 0
        var endPoint = 0
        val sortedAllLocations: MutableList<Location> = mutableListOf()
        val sortedImportantLocations: MutableList<Location> = mutableListOf()
        val allLocations: MutableMap<Int, Location> = mutableMapOf()
        val importantLocations: MutableMap<Int, Location> = mutableMapOf()

        val queryGetAllTransfersForLocation = """
            SELECT t.location_from, t.location_to, l1.id, l1.name, l1.important
            FROM transfers AS t
            LEFT JOIN locations AS l1 
            ON t.location_from = l1.id 
            LEFT JOIN locations AS l2
            ON t.location_to = l2.id 
            LEFT JOIN places AS p1
            ON l1.places_id = p1.id 
            LEFT JOIN places as p2
            ON l2.places_id = p2.id
            WHERE p1.id = $currentPlaceId
            AND p2.id = $currentPlaceId
        """

        val queryStartPoint = """
            SELECT t.*, p1.id as P1_ID 
            FROM transfers AS t
            LEFT JOIN locations AS l1 
            ON t.location_from = l1.id 
            LEFT JOIN places AS p1
            ON l1.places_id = p1.id
            WHERE p1.id = $currentPlaceId
            AND t.location_to = -1
            """

        val queryEndPoint = """
            SELECT t.*, p2.id as P2_ID 
            FROM transfers AS t
            LEFT JOIN locations AS l2 
            ON t.location_to = l2.id 
            LEFT JOIN places AS p2
            ON l2.places_id = p2.id
            WHERE p2.id = $currentPlaceId
            AND t.location_from = -1
            """

        db?.rawQuery(queryGetAllTransfersForLocation, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val from = cursor.getInt(cursor.getColumnIndexOrThrow("location_from"))
                    val to = cursor.getInt(cursor.getColumnIndexOrThrow("location_to"))

                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val important = cursor.getInt(cursor.getColumnIndexOrThrow("important"))
                    val isImportant = (important == 1)
                    val texts = getTexts("locations_id", id)
                    val detailedText = texts?.get(true)
                    val conciseText = texts?.get(false)


                    Log.i("TourManager", "Transfer: $from -> $to")
                    transfersForLocation[from] = to
                    allLocations[from] = Location(name, getItems(id), detailedText, conciseText)
                    if (isImportant) {
                        importantLocations[from] =
                            Location(name, getItems(id), detailedText, conciseText)
                    }
                } while (cursor.moveToNext())
            }
        }
        db?.rawQuery(queryStartPoint, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                startPoint = cursor.getInt(cursor.getColumnIndexOrThrow("location_from"))
                Log.i("TourManager", "Start Point: $startPoint")
            }
        }
        db?.rawQuery(queryEndPoint, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                endPoint = cursor.getInt(cursor.getColumnIndexOrThrow("location_to"))
                Log.i("TourManager", "Start Point: $endPoint")
            }
        }

        var currentStartpoint = startPoint
        do {

            val currentEndpoint = transfersForLocation[currentStartpoint]!!

            sortedAllLocations.add(allLocations[currentStartpoint]!!)

            if (importantLocations.containsKey(currentStartpoint)) {
                sortedImportantLocations.add(importantLocations[currentStartpoint]!!)
            }

            currentStartpoint = currentEndpoint
        } while (currentEndpoint != endPoint)


        Log.i("TourManager", "Transfers for location: $transfersForLocation")

        return Pair(sortedAllLocations, sortedImportantLocations)

    }

    /**
     * Method to get all items for a location.
     */
    private fun getItems(locationId: Int): List<Item> {
        val items = mutableListOf<Item>()
        val query = "SELECT * FROM items WHERE locations_id = $locationId"
        db?.rawQuery(query, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val texts = getTexts("items_id", id)
                    val detailedText = texts?.get(true)
                    val conciseText = texts?.get(false)
                    items.add(Item(name, detailedText, conciseText))
                } while (cursor.moveToNext())
            }
        }
        return items
    }

    /**
     * Method to get texts for a specific item.
     * @param field The field to search for
     * @param itemId The item id to search for
     */
    private fun getTexts(field: String, itemId: Int): Map<Boolean, Text>? {
        val query = "SELECT * FROM texts WHERE $field = $itemId"
        val texts = mutableMapOf<Boolean, Text>()
        db?.rawQuery(query, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val text = cursor.getString(cursor.getColumnIndexOrThrow("text"))
                    val media = getMedia(cursor.getInt(cursor.getColumnIndexOrThrow("id")))
                    val isDetailed = cursor.getInt(cursor.getColumnIndexOrThrow("detailed")) == 1
                    texts[isDetailed] = Text(text, media)
                } while (cursor.moveToNext())
            }
            return texts
        }
        return null
    }

    private fun getMedia(textId: Int): Media? {
        val query = "SELECT * FROM media WHERE texts_id = $textId"
        db?.rawQuery(query, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val url = cursor.getString(cursor.getColumnIndexOrThrow("url"))
                return Media(URL(url))
            }
        }
        return null
    }
}