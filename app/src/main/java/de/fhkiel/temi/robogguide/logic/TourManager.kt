package de.fhkiel.temi.robogguide.logic

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import de.fhkiel.temi.robogguide.models.Place
import de.fhkiel.temi.robogguide.models.Tour

/**
 * Class to manage tours.
 * Parses the database and provides methods to further choose a specific [Tour].
 * The specific [Tour] instance can be used to guide the robot.
 * @param db SQLiteDatabase instance
 *
 */
class TourManager(private val db: SQLiteDatabase?) {

    private val _tours: MutableList<Tour> = mutableListOf()
    private var _currentPlace: Place? = null
    val allPlaces: MutableList<Place> = mutableListOf()
    var error: Exception? = null

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
        Log.i("TourManager", "Checking database for validity")

        if (db == null) {
            Log.e("TourManager", "Database is not initialized")
            throw IllegalStateException("Database is not initialized")
        }

        val places = db.rawQuery("SELECT * FROM places", null)
        val locations = db.rawQuery("SELECT * FROM locations", null)
        val transfers = db.rawQuery("SELECT * FROM transfers", null)

        places.use {
            if (it.count == 0) {
                Log.e("TourManager", "Places is empty")
                throw IllegalStateException("Places is empty")
            }
            if (it.moveToFirst()) {
                do {
                    val name = it.getString(it.getColumnIndexOrThrow("name"))
                    Log.i("TourManager", "Place: $name")
                    allPlaces.add(Place(name))
                } while (it.moveToNext())
            }
        }

        val locationIds = mutableSetOf<String>()
        val startLocations = mutableSetOf<String>()
        val endLocations = mutableSetOf<String>()

        locations.use {
            if (it.count == 0) {
                Log.e("TourManager", "Locations is empty")
                throw IllegalStateException("Locations is empty")
            }
            if (it.moveToFirst()) {
                do {
                    val locationId = it.getString(it.getColumnIndexOrThrow("id"))
                    locationIds.add(locationId)
                    Log.i("TourManager", "Location ID: $locationId")
                } while (it.moveToNext())
            }
        }

        val fromCount = mutableMapOf<String, Int>()
        val toCount = mutableMapOf<String, Int>()

        transfers.use {
            if (it.count == 0) {
                Log.e("TourManager", "Transfers is empty")
                throw IllegalStateException("Transfers is empty")
            }
            if (it.moveToFirst()) {
                do {
                    val from = it.getString(it.getColumnIndexOrThrow("location_from"))
                    val to = it.getString(it.getColumnIndexOrThrow("location_to"))

                    if (!locationIds.contains(from) || !locationIds.contains(to)) {
                        Log.e("TourManager", "Invalid transfer: $from -> $to")
                        throw IllegalStateException("Invalid transfer: $from -> $to")
                    }

                    fromCount[from] = fromCount.getOrDefault(from, 0) + 1
                    toCount[to] = toCount.getOrDefault(to, 0) + 1

                    startLocations.add(from)
                    endLocations.add(to)
                    Log.i("TourManager", "Transfer: $from -> $to")
                } while (it.moveToNext())
            }
        }

        fromCount.forEach { (id, count) ->
            if (count > 1) {
                Log.e("TourManager", "ID $id appears more than once in the 'from' column")
                throw IllegalStateException("ID $id appears more than once in the 'from' column")
            }
        }

        toCount.forEach { (id, count) ->
            if (count > 1) {
                Log.e("TourManager", "ID $id appears more than once in the 'to' column")
                throw IllegalStateException("ID $id appears more than once in the 'to' column")
            }
        }

        val startLocation = startLocations.subtract(endLocations)
        val endLocation = endLocations.subtract(startLocations)

        if (startLocation.size != 1 || endLocation.size != 1) {
            Log.e("TourManager", "Invalid number of start or end locations")
            throw IllegalStateException("Invalid number of start or end locations")
        }

        Log.i("TourManager", "Start location: ${startLocation.first()}")
        Log.i("TourManager", "End location: ${endLocation.first()}")

        // Check for the start location again to stop as it indicates another tour
        db.rawQuery("SELECT * FROM transfers", null).use { newTransfers ->
            if (newTransfers.moveToFirst()) {
                do {
                    val from = newTransfers.getString(newTransfers.getColumnIndexOrThrow("location_from"))
                    if (from == startLocation.first()) {
                        Log.i("TourManager", "Encountered start location again, stopping check")
                        break
                    }
                } while (newTransfers.moveToNext())
            }
        }

        Log.i("TourManager", "Database is valid")
    }

}