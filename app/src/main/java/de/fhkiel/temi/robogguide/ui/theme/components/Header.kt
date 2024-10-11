package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 64.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Box(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}
