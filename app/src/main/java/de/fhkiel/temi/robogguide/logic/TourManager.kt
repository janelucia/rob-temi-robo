package de.fhkiel.temi.robogguide.logic

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import de.fhkiel.temi.robogguide.models.LevelOfDetail
import de.fhkiel.temi.robogguide.models.Tour

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
            Log.e("TourManager", "Database is not initialized")
            throw IllegalStateException("Database is not initialized")
        }

        LevelOfDetail.EVERYTHING_DETAILED.setLengthInMinutes(60)
        LevelOfDetail.EVERYTHING_DETAILED.setNrOfExhibits(10)

        Log.i("TourManager", LevelOfDetail.EVERYTHING_DETAILED.getNrOfExhibits().toString())



        Log.i("TourManager", "Database is valid")
    }

}