package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import de.fhkiel.temi.robogguide.models.Text

@Composable
fun TextDisplay(text: Text, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        androidx.compose.material3.Text(text = text.value)
        text.media?.let { media ->
            Image(
                painter = rememberAsyncImagePainter(model = media.url),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
}