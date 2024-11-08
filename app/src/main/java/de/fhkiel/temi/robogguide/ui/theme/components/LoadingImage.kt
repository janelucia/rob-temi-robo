package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ErrorResult
import de.fhkiel.temi.robogguide.R

@Composable
fun LoadingImage(urlString: String, modifier: Modifier = Modifier.fillMaxWidth(), contentScale: ContentScale = ContentScale.FillBounds) {

    var isLoading by remember { mutableStateOf(false) }
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        AsyncImage(
            model = urlString,
            contentDescription = "Exponat Image",
            modifier = modifier,
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
}