package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ErrorResult
import de.fhkiel.temi.robogguide.R

/**
 * LoadingImage
 * - Displays an image with a loading indicator
 * @param urlString: URL of the image
 * @param modifier: Modifier for the image
 * @param contentScale: Content scale of the image, like fit or crop
 */

@Composable
fun LoadingImage(
    urlString: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    contentScale: ContentScale = ContentScale.FillBounds
) {
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        AsyncImage(
            model = urlString,
            contentDescription = "Exponat Image",
            modifier = modifier.clickable(onClick = { showDialog = true }),
            contentScale = contentScale,
            onLoading = {
                isLoading = true
                Log.i("Exhibit", "Loading image")
            },
            onSuccess = {
                isLoading = false
                Log.i("Exhibit", "Image loaded")
            },
            onError = { error ->
                isLoading = false
                val errorResult: ErrorResult = error.result
                Log.e("Exhibit", "Error loading image. ${errorResult.throwable.message.toString()}")
            },
            error = painterResource(id = R.drawable.computermuseum_maerz_23),
        )
        if (isLoading) {
            CircularProgressIndicator()
        }
    }
    // open the image in fullscreen
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)) {
                AsyncImage(
                    model = urlString,
                    contentDescription = "Fullscreen Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    error = painterResource(id = R.drawable.computermuseum_maerz_23),
                )
                CustomIconButton(
                    iconId = R.drawable.baseline_cancel_24,
                    onClick = { showDialog = false },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }

}