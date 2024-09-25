package de.fhkiel.temi.robogguide.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.json.JSONObject
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DatabaseHelper(context: Context, databaseName: String) : SQLiteOpenHelper(context, databaseName, null, 1) {

    private val appContext = context

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    private fun getFullDatabasePath(): String {
        return "${appContext.packageName}/database/$databaseName"
    }


    // Method to copy the database from assets to internal storage
    @Throws(IOException::class)
    private fun copyDatabase() {
        val inputStream: InputStream = appContext.assets.open(databaseName)
        val outFileName = getFullDatabasePath()
        val outputStream: OutputStream = FileOutputStream(outFileName)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    // Method to check if the database already exists in internal storage
    private fun checkDatabase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            checkDB = SQLiteDatabase.openDatabase(getFullDatabasePath(), null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: Exception) {
            // Database does not exist yet
            Log.e(appContext.packageName, "Database $databaseName not found!")
        }

        checkDB?.close()
        return checkDB != null
    }

    // Method to initialize the database
    @Throws(IOException::class)
    fun initializeDatabase() {
        if (!checkDatabase()) {
            this.readableDatabase // Creates an empty database in the default system path
            copyDatabase() // Copies the database from assets
        }
    }

    // Method to retrieve the table structure dynamically
    private fun getTableStructure(tableName: String): List<String> {
        val columns = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("PRAGMA table_info($tableName)", null)

        if (cursor.moveToFirst()) {
            do {
                // Retrieve the column name from the result
                val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                columns.add(columnName)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return columns
    }

    // Method to read data dynamically and convert it into JSON
    @Suppress("unused")
    fun getTableDataAsJson(tableName: String): MutableList<JSONObject> {
        val db = this.readableDatabase
        val columns = getTableStructure(tableName)

        val cursor = db.rawQuery("SELECT * FROM $tableName", null)
        val jsonArray = mutableListOf<JSONObject>()

        if (cursor.moveToFirst()) {
            do {
                val jsonObject = JSONObject()
                for (column in columns) {
                    val value = cursor.getString(cursor.getColumnIndexOrThrow(column))
                    jsonObject.put(column, value)
                }
                jsonArray.add(jsonObject)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return jsonArray
    }
}
