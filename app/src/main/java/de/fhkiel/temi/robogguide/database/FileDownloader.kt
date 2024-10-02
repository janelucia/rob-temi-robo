package de.fhkiel.temi.robogguide.database

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.net.ssl.HttpsURLConnection

class FileDownloader {

    private fun fetchUrlData(urlString: String): String? {
        var result: String? = null
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"

            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            reader.forEachLine { line ->
                response.append(line)
            }
            result = response.toString()

            reader.close()
            inputStream.close()
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    suspend fun downloadLatestDatabaseFile(url: String, downloadDirectory: File): DownloadTask {
        val downloadTask = DownloadTask()

        withContext(Dispatchers.IO) {
            try {
                // 1. Fetch the list of files from the URL
                val showUrl = "$url/show.php"
                val data = fetchUrlData(showUrl)
                Log.i("DATA", "$data")

                val body = "" //response.body?.string() ?: throw IOException("Empty response received")

                // 2. Find the file with the latest date in the name
                val latestFileName = findLatestFileName(body)
                    ?: throw IOException("No matching file found")

                val downloadUrl = "$url/$latestFileName"

                // 3. Download and save the file
                downloadFile(downloadUrl, latestFileName, downloadDirectory, downloadTask)

            } catch (e: Exception) {
                downloadTask.error = e
            }
        }

        return downloadTask
    }

    // Helper function to find the filename with the latest date
    private fun findLatestFileName(fileList: String): String? {
        val regex = Regex("_(\\d{4}-\\d{2}-\\d{2}_\\d{2}:\\d{2}:\\d{2})\\.db")
        var latestDate: Date? = null
        var latestFileName: String? = null

        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())

        fileList.split("\n").forEach { line ->
            val match = regex.find(line)
            if (match != null) {
                val dateStr = match.groupValues[1]
                val fileDate = dateFormat.parse(dateStr)
                if (fileDate != null && (latestDate == null || fileDate.after(latestDate))) {
                    latestDate = fileDate
                    latestFileName = line.trim()
                }
            }
        }
        return latestFileName
    }

    // Function to handle the download of the file and track progress
    private fun downloadFile(url: String, fileName: String, downloadDirectory: File, downloadTask: DownloadTask) {
//        val request = Request.Builder().url(url).build()

//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                downloadTask.error = e
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (!response.isSuccessful) {
//                    downloadTask.error = IOException("Download error: ${response.code}")
//                    return
//                }
//
//                try {
//                    val file = File(downloadDirectory, fileName)
//                    val fos = FileOutputStream(file)
//                    val inputStream = response.body?.byteStream()
//                    val buffer = ByteArray(2048)
//                    var bytesRead: Int
//
//                    var totalBytesRead = 0L
//                    val contentLength = response.body?.contentLength() ?: -1
//
//                    // Read the input stream and write to file
//                    while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
//                        fos.write(buffer, 0, bytesRead)
//                        totalBytesRead += bytesRead
//                        downloadTask.progress = if (contentLength != -1L) {
//                            (totalBytesRead * 100 / contentLength).toInt()
//                        } else {
//                            -1 // Unknown size
//                        }
//                    }
//
//                    fos.close()
//                    inputStream?.close()
//
//                    downloadTask.file = file
//
//                } catch (e: IOException) {
//                    downloadTask.error = e
//                }
//            }
//        })
    }
}

// Class to represent the download task and allow progress tracking
class DownloadTask {
    @Volatile
    var progress: Int = 0 // Progress in percentage

    @Volatile
    var file: File? = null // The downloaded file

    @Volatile
    var error: Exception? = null // Error, if any occurred
}
