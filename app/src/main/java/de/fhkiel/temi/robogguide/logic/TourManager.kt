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
    private val fromCount = mutableMapOf<String, Int>()
    private val toCount = mutableMapOf<String, Int>()
    private val errorMessage : String = "Die Datenbank scheint nicht korrekt befüllt zu sein.\nFolgender Fehler ist aufgetreten:\n"

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
            throw IllegalStateException(errorMessage + "Datenbank ist nicht initialisiert.")
        }

        val places = db.rawQuery("SELECT * FROM places", null)
        val locations = db.rawQuery("SELECT * FROM locations", null)
        val transfers = db.rawQuery("SELECT * FROM transfers", null)

        places.use {
            if (it.count == 0) {
                Log.e("TourManager", "Places is empty")
                throw IllegalStateException(errorMessage + "Es sind keine Karten (Places) vorhanden.")
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
                throw IllegalStateException(errorMessage + "Es sind keine Orte (Locations) vorhanden.")
            }
            if (it.moveToFirst()) {
                do {
                    val locationId = it.getString(it.getColumnIndexOrThrow("id"))
                    locationIds.add(locationId)
                    Log.i("TourManager", "Location ID: $locationId")
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
                    val from = it.getString(it.getColumnIndexOrThrow("location_from"))
                    val to = it.getString(it.getColumnIndexOrThrow("location_to"))

                    if (!locationIds.contains(from) || !locationIds.contains(to)) {
                        Log.e("TourManager", "Invalid transfer: $from -> $to")
                        throw IllegalStateException(errorMessage + "Ungültige Verbindung (Transfer) zwischen den Orten: $from -> $to")
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