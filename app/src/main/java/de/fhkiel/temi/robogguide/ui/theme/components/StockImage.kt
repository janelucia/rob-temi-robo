package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.fhkiel.temi.robogguide.R

@Composable
fun StockImage() {
    Image(
        painter = painterResource(id = R.drawable.computermuseum_maerz_23),
        contentDescription = "Computer Museum",
        modifier = Modifier.size(400.dp),
        contentScale = ContentScale.FillHeight
    )
}