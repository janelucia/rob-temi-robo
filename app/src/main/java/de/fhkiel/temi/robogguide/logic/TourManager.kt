package de.fhkiel.temi.robogguide.logic

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import de.fhkiel.temi.robogguide.database.DatabaseHelper

/**
 * Class to manage tours.
 * Parses the database and provides methods to further choose a specific [Tour].
 * The specific [Tour] instance can be used to guide the robot.
 * @param db SQLiteDatabase instance
 *
 */
class TourManager(private val db: SQLiteDatabase?) {

    private val tours: MutableList<Tour> = mutableListOf()

    init {
        checkDatabase()
        parseTours()
    }

    /**
     * Method to parse the database and create [Tour] instances.
     */
    private fun parseTours() {
        Log.i("TourManager", "Parsing tours")

    }

    private fun checkDatabase() {
        Log.i("TourManager", "Checking database for validity")
        if (db == null) {
            throw IllegalStateException("Database is not initialized")
        }

        db.rawQuery("SELECT * FROM places", null).use { cursor ->
            if (cursor.count == 0) {
                throw IllegalStateException("Places table is empty")
            }
            cursor.columnNames.forEach {
                Log.d("TourManager", it)
            }
            Log.d("TourManager", cursor.position.toString())
        }
        Log.i("TourManager", "Database is valid")
    }

}