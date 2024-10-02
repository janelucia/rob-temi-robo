package de.fhkiel.temi.robogguide.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DynamicDatabaseReader(context: Context, private val databaseName: String) : SQLiteOpenHelper(context, databaseName, null, 1) {

    private val databasePath = File(context.getDatabasePath(databaseName).path).absolutePath
    private val databaseFullPath = "$databasePath$databaseName"
    private var database: SQLiteDatabase? = null
    private val appContext: Context = context

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    // Method to copy the database from assets to internal storage (always overwrites existing database)
    @Throws(IOException::class)
    private fun copyDatabase() {
        val inputStream: InputStream = appContext.assets.open(databaseName)
        val outFileName = databaseFullPath
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

    // Method to initialize the database (always copies and overwrites any existing database)
    @Throws(IOException::class)
    fun initializeDatabase() {
        this.readableDatabase // Create an empty database in the default system path
        copyDatabase() // Overwrite the existing database with the one from assets
        openDatabase() // Open the copied database
    }

    // Method to open the copied database and store its reference
    private fun openDatabase() {
        database = SQLiteDatabase.openDatabase(databaseFullPath, null, SQLiteDatabase.OPEN_READWRITE)
    }

    // Method to close the opened database
    fun closeDatabase() {
        database?.close()
    }

    suspend fun getDatabaseUpdate(): DownloadTask {
        val downloader = FileDownloader()
        val downloadDirectory = File(appContext.filesDir, "downloads")
        downloadDirectory.mkdirs()

        return downloader.downloadLatestDatabaseFile(url = "https://roboguide.wissjahr.uber.space/releases", downloadDirectory = downloadDirectory)
    }

    // Method to retrieve the table structure dynamically, including the primary key
    private fun getTableStructure(tableName: String): Pair<List<String>, String?> {
        val columns = mutableListOf<String>()
        var primaryKey: String? = null

        database?.let { db ->
            val cursor = db.rawQuery("PRAGMA table_info(`$tableName`)", null)

            if (cursor.moveToFirst()) {
                do {
                    // Retrieve the column name from the result
                    val columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    columns.add(columnName)

                    // Check if this column is the primary key
                    val isPrimaryKey = cursor.getInt(cursor.getColumnIndexOrThrow("pk")) == 1
                    if (isPrimaryKey) {
                        primaryKey = columnName
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }

        return Pair(columns, primaryKey)
    }

    // Method to read data dynamically and convert it into a JSON Map using the primary key as the key
    @Suppress("unused")
    fun getTableDataAsJson(tableName: String): Map<String, JSONObject> {
        val (columns, primaryKey) = getTableStructure(tableName)

        if (primaryKey == null) {
            throw IllegalArgumentException("Table $tableName has no primary key.")
        }

        val jsonMap = mutableMapOf<String, JSONObject>()

        database?.let { db ->
            val cursor = db.rawQuery("SELECT * FROM `$tableName`", null)

            if (cursor.moveToFirst()) {
                do {
                    val jsonObject = JSONObject()
                    var primaryKeyValue: String? = null

                    for (column in columns) {
                        val value = cursor.getString(cursor.getColumnIndexOrThrow(column))
                        jsonObject.put(column, value)

                        // Check if this column is the primary key and save its value
                        if (column == primaryKey) {
                            primaryKeyValue = value
                        }
                    }

                    // Ensure the primary key value is not null before adding to the map
                    primaryKeyValue?.let {
                        jsonMap[it] = jsonObject
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }

        return jsonMap
    }
}
