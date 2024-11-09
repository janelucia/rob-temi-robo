package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import de.fhkiel.temi.robogguide.models.Text

/**
 * Composable function to display a list of media items.
 * @param texts The list of texts to display. Sepereated by commas.
 */
@Composable
fun DisplayMediaList(vararg texts: Text?) {
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        texts.forEach { text ->
            text?.mediaList?.forEach { media ->
                item {
                    if (media.url.toString().isEmpty()) {
                        StockImage()
                    } else if (media.url.host == "www.youtube.com") {
                        Log.i("Exhibit", "Video kann ich noch nicht")
                    } else {
                        LoadingImage(
                            urlString = media.url.toString(),
                            modifier = Modifier.size(400.dp),
                            contentScale = ContentScale.FillBounds
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
    }
}
