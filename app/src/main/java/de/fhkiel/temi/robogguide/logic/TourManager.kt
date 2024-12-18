package de.fhkiel.temi.robogguide.logic

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.models.Media
import de.fhkiel.temi.robogguide.models.MediaType
import de.fhkiel.temi.robogguide.models.Place
import de.fhkiel.temi.robogguide.models.Text
import de.fhkiel.temi.robogguide.models.Transfer
import java.net.URL

/**
 * TourManager: manages the tour and the database.
 * @param db The database to use
 */
class TourManager(private val db: SQLiteDatabase?) {

    private var currentPlaceId: Int? = null
    private var currentPlaceName: String? = null
    val allPlacesMap: MutableMap<Int, String> = mutableMapOf()

    val error = MutableLiveData<Exception>(null)

    var doneChecking = false

    private val tourStartLocation = mutableMapOf<Int, Int>()
    private val tourEndLocation = mutableMapOf<Int, Int>()

    var selectedPlace: Place? = null

    init {
        // try catch to handle an error like a wrongly named database
        try {
            checkDatabase()
            doneChecking = true
        } catch (e: Exception) {
            error.value = e
            Log.e("TourManager", "Error initializing TourManager: ${e.message}")
        }
    }

    /**
     * Method to check the database for validity.
     */
    private fun checkDatabase() {

        val locationIds = mutableSetOf<Int>()
        val startLocations = mutableSetOf<Int>()
        val endLocations = mutableSetOf<Int>()
        val fromCount = mutableMapOf<Int, MutableMap<Int, Int>>()
        val toCount = mutableMapOf<Int, MutableMap<Int, Int>>()
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

                    if (p1 != p2) {
                        Log.e("TourManager", "Error in Transfer: $from -> $to")
                        throw IllegalStateException(errorMessage + "Die Transfers scheinen zwischen mehreren Places zu laufen. $from -> $to")
                    }

                    if (!locationIds.contains(from) || !locationIds.contains(to)) {
                        Log.e("TourManager", "Invalid transfer: $from -> $to")
                        throw IllegalStateException(errorMessage + "Ungültige Verbindung (Transfer) zwischen den Orten: $from -> $to")
                    }

                    if (fromCount[p1].isNullOrEmpty()) {
                        fromCount[p1] = mutableMapOf()
                    }
                    if (toCount[p2].isNullOrEmpty()) {
                        toCount[p2] = mutableMapOf()
                    }

                    val fromMap = fromCount[p1]!!
                    val toMap = toCount[p2]!!

                    fromMap[from] = fromMap.getOrDefault(from, 0) + 1
                    toMap[to] = toMap.getOrDefault(to, 0) + 1

                    startLocations.add(from)
                    endLocations.add(to)
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
                        // TODO einkommentieren, falls man eine strenge Überprüfung wünscht.
                        //throw IllegalStateException(errorMessage + "Der Text mit der ID $id hat keine gültige ID Zuweisung.")
                    }

                } while (it.moveToNext())
            }
        }

        fromCount.forEach { (placeId, map) ->

            val toMapKeys = toCount[placeId]!!.keys

            // compare which key is not in the other map
            val startLocationsDiff = map.keys.subtract(toMapKeys)

            if (startLocationsDiff.size > 1 || startLocationsDiff.isEmpty()) {
                Log.e("TourManager", "Invalid number of start locations")
                throw IllegalStateException(errorMessage + "Ungültige Anzahl von Startorten in place $placeId.")
            } else {
                tourStartLocation[placeId] = startLocationsDiff.first()
            }

            map.forEach { (id, count) ->
                if (count > 1) {
                    Log.e("TourManager", "ID $id appears more than once in the 'from' column")
                    throw IllegalStateException(errorMessage + "ID $id erscheint mehr als einmal in der 'from' Spalte von der Verbindungstabelle (transfers).")
                }
            }
        }

        toCount.forEach { (placeId, map) ->

            val fromMapKeys = fromCount[placeId]!!.keys

            // compare which key is not in the other map
            val endLocationsDiff = map.keys.subtract(fromMapKeys)

            if (endLocationsDiff.size > 1 || endLocationsDiff.isEmpty()) {
                Log.e("TourManager", "Invalid number of start locations")
                throw IllegalStateException(errorMessage + "Ungültige Anzahl von Startorten in place $placeId.")
            } else {
                tourEndLocation[placeId] = endLocationsDiff.first()
            }

            map.forEach { (id, count) ->
                if (count > 1) {
                    Log.e("TourManager", "ID $id appears more than once in the 'to' column")
                    throw IllegalStateException(errorMessage + "ID $id erscheint mehr als einmal in der 'to' Spalte von der Verbindungstabelle (transfers).")
                }
            }

        }

        val startLocation = startLocations.subtract(endLocations)
        val endLocation = endLocations.subtract(startLocations)

        if (startLocation.size != 1 || endLocation.size != 1) {
            Log.e("TourManager", "Invalid number of start or end locations")
            throw IllegalStateException(errorMessage + "Ungültige Anzahl von Start- oder Endorten.")
        }

        Log.d("TourManager", "Start location: ${startLocation.first()}")
        Log.d("TourManager", "End location: ${endLocation.first()}")

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

    /**
     * Method to set the current place.
     * @param placeId The place id to set
     * @param placeName The place name to set
     * @return True if the place was set successfully, false otherwise
     */
    fun setPlace(placeId: Int, placeName: String): Boolean {
        Log.i("TourManager", "Set current place to $placeName")
        currentPlaceId = placeId
        currentPlaceName = placeName
        fillThePlaceWithData()
        try {
            checkPlaceLocationsWithRobot()
        } catch (e: Exception) {
            error.value = e
            Log.e("TourManager", "Error checking place locations with robot: ${e.message}")
            return false
        }
        return true
    }

    private fun fillThePlaceWithData() {
        Log.i("TourManager", "Filling the selected place with data")

        val (allLocations, importantLocations, allTransfers) = getLocations()
        selectedPlace = Place(currentPlaceName!!, importantLocations, allLocations, allTransfers)
    }

    /**
     * Method to get all locations for the current place.
     */
    private fun getLocations(): Triple<List<Location>, List<Location>, Map<String, Transfer>> {

        val transfersForLocation: MutableMap<Int, Int> = mutableMapOf()

        if (tourStartLocation.containsKey(currentPlaceId) && tourEndLocation.containsKey(currentPlaceId)) {
            Log.i("TourManager", "Start and end location found")
        } else {
            Log.e("TourManager", "Start and end location not found")
            throw IllegalStateException("Es konnte kein Start oder Endpunkt für diesen Ort gefunden werden.")
        }

        val startPoint = tourStartLocation[currentPlaceId]
        val endPoint = tourEndLocation[currentPlaceId]
        val sortedAllLocations: MutableList<Location> = mutableListOf()
        val sortedImportantLocations: MutableList<Location> = mutableListOf()
        val allLocations: MutableMap<Int, Location> = mutableMapOf()
        val importantLocations: MutableMap<Int, Location> = mutableMapOf()
        val allTransfers: MutableMap<String, Transfer> = mutableMapOf()

        val queryGetAllTransfersForLocation = """
            SELECT t.id as transfer_id, 
            t.location_from, t.location_to, l1.id, l1.name as name_from, l1.important as from_important, l2.important as to_important, l2.name as name_to
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

        db?.rawQuery(queryGetAllTransfersForLocation, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val from = cursor.getInt(cursor.getColumnIndexOrThrow("location_from"))
                    val to = cursor.getInt(cursor.getColumnIndexOrThrow("location_to"))

                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val transferId = cursor.getInt(cursor.getColumnIndexOrThrow("transfer_id"))

                    val nameTo = cursor.getString(cursor.getColumnIndexOrThrow("name_to"))
                    val nameFrom = cursor.getString(cursor.getColumnIndexOrThrow("name_from"))
                    val important = cursor.getInt(cursor.getColumnIndexOrThrow("from_important"))

                    val transferTexts = getTexts("transfers_id", transferId)
                    val detailedTransferText = transferTexts?.get(true)
                    val conciceTransferText = transferTexts?.get(false)
                    val isImportant = (important == 1)
                    val texts = getTexts("locations_id", id)
                    val detailedText = texts?.get(true)
                    val conciseText = texts?.get(false)

                    allTransfers[nameTo] = Transfer(detailedTransferText, conciceTransferText)
                    transfersForLocation[from] = to

                    val newLocation = Location(
                        nameFrom,
                        getItems(id),
                        detailedText,
                        conciseText,
                    )

                    newLocation.fillYourItemsWithYourself()

                    allLocations[from] = newLocation

                    if (isImportant) {
                        importantLocations[from] = newLocation
                    }
                } while (cursor.moveToNext())

                // after the loop, add the last location to the list
                cursor.moveToLast()
                val to = cursor.getInt(cursor.getColumnIndexOrThrow("location_to"))
                val nameTo = cursor.getString(cursor.getColumnIndexOrThrow("name_to"))
                val important = cursor.getInt(cursor.getColumnIndexOrThrow("to_important"))
                val isImportant = (important == 1)

                val texts = getTexts("locations_id", to)
                val detailedText = texts?.get(true)
                val conciseText = texts?.get(false)

                val newLocation = Location(
                    nameTo,
                    getItems(to),
                    detailedText,
                    conciseText,
                )

                newLocation.fillYourItemsWithYourself()

                allLocations[to] = newLocation

                if (isImportant) {
                    importantLocations[to] = newLocation
                }
                allLocations.forEach() { (key, value) ->
                    Log.i("TourManager", "Location: $key -> ${value.name} ${value.items}")
                }
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

        // add the end location to the list
        sortedAllLocations.add(allLocations[endPoint]!!)
        if (importantLocations.containsKey(endPoint)) {
            sortedImportantLocations.add(importantLocations[endPoint]!!)
        }

        Log.d("TourManager", "All sorted locations: ${sortedAllLocations.size}")

        Log.i("TourManager", "Transfers for location: $transfersForLocation")

        return Triple(sortedAllLocations, sortedImportantLocations, allTransfers)

    }

    /**
     * Method to get all items for a location.
     */
    private fun getItems(locationId: Int): MutableList<Item> {
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

    /**
     * Method to get media for a specific text.
     * @param textId The text id to search for
     * @return The media object with its URL
     */
    private fun getMedia(textId: Int): List<Media> {
        val result: MutableList<Media> = mutableListOf()
        val query = "SELECT * FROM media WHERE texts_id = $textId"
        db?.rawQuery(query, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val urlString = cursor.getString(cursor.getColumnIndexOrThrow("url"))
                    val url = URL(urlString)
                    val type: MediaType =
                        if (url.host == "www.youtube.com" ||
                            url.host == "youtube.com" ||
                            url.host == "youtu.be") {
                            MediaType.VIDEO
                        } else {
                            MediaType.IMAGE
                        }
                    result.add(Media(url, type))
                } while (cursor.moveToNext())

            }
        }
        return result
    }

    /**
     * Method to check if the place locations are on the robot.
     */
    private fun checkPlaceLocationsWithRobot() {
        val mRobot = Robot.getInstance()
        Log.i("TourManager", "Checking place locations with robot")

        assert(selectedPlace != null)
        selectedPlace!!.allLocations.forEach { location ->
            if (!mRobot.locations.contains(location.name)) {
                Log.e("TourManager", "Location ${location.name} not found on robot")
                throw IllegalStateException("Der Ort ${location.name} konnte nicht auf dem Roboter gefunden werden.")
            }
        }
    }
}