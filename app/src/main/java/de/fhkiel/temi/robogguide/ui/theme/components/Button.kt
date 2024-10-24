package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 600.dp,
    height: Dp = 200.dp,
    backgroundColor: Color = Color.Black,
    contentColor: Color = Color.White,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    fontSize: TextUnit = 64.sp
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(width)
            .height(height).border(borderWidth, borderColor, shape = ButtonDefaults.outlinedShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
    ) {
        Text(
            text = title,
            fontSize = fontSize,
            lineHeight = fontSize,
            textAlign = TextAlign.Center
        )
    }
}
