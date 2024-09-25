package de.fhkiel.temi.robogguide

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONObject

class DynamicDatabaseReader(context: Context, databaseName: String) : SQLiteOpenHelper(context, databaseName, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Example to create a table
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

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
