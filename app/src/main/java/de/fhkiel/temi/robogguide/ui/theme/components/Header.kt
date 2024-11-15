package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Header
 * - Displays a header with a title
 * @param title: Title of the header
 * @param modifier: Modifier for the header
 * @param fontSize: Font size of the title
 * @param fontWeight: Font weight of the title
 */
@Composable
fun Header(
    title: String?,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 100.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Box(modifier = modifier) {
        if (title != null) {
            Text(
                text = title,
                modifier = Modifier.align(Alignment.Center),
                fontSize = fontSize,
                fontWeight = fontWeight,
                lineHeight = fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}
