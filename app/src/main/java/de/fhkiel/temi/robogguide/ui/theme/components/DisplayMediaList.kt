package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import de.fhkiel.temi.robogguide.models.MediaType
import de.fhkiel.temi.robogguide.models.Text

/**
 * Composable function to display a list of media items.
 * @param texts The list of texts to display. Sepereated by commas.
 */
@Composable
fun DisplayMediaList(vararg texts: Text?) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        texts.forEach { text ->
            text?.mediaList?.forEach { media ->
                item {
                    Box (
                        modifier = Modifier.padding(16.dp).size(800.dp)
                    ) {
                        when (media.type) {
                            MediaType.VIDEO -> {
                                Log.d("DisplayMediaList", "Displaying video: ${media.url}")
                                LoadingVideo(
                                    url = media.url,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            MediaType.IMAGE -> {
                                Log.d("DisplayMediaList", "Displaying image: ${media.url}")
                                LoadingImage(
                                    urlString = media.url.toString(),
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
