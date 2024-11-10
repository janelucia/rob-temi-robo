package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.fhkiel.temi.robogguide.models.MediaType
import de.fhkiel.temi.robogguide.models.Text as TextModel

/**
 * Composable function to display a list of media items.
 * @param texts The list of texts to display. Sepereated by commas.
 */
@Composable
fun DisplayMediaList(vararg texts: TextModel?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.size(700.dp)
        ) {
            texts.forEach { text ->
                text?.mediaList?.forEach { media ->
                    item {
                        Box(
                            modifier = Modifier.padding(16.dp).fillMaxSize()
                        ) {
                            when (media.type) {
                                MediaType.VIDEO -> {
                                    Log.d("DisplayMediaList", "Displaying video: ${media.url}")
                                    //TODO: Implement video player
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
        Spacer(modifier = Modifier.width(16.dp))
        LazyColumn(
            modifier = Modifier.size(700.dp)
        ) {
            texts.forEach { text ->
                item {
                    Text(
                        text = text?.value ?: "",
                        color = Color.Black,
                        fontSize = 48.sp,
                        lineHeight = 48.sp,
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    )
                }
            }
        }
    }
}
