package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import de.fhkiel.temi.robogguide.models.Text as TextModel

@Composable
fun TextDisplay(text: TextModel, modifier: Modifier = Modifier, fontSize: TextUnit = 16.sp) {
    Column(modifier = modifier) {
        Text(text = text.value,
            fontSize = fontSize
            )
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