package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CustomButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 600.dp,
    height: Dp = 200.dp,
    initialBackgroundColor: Color = Color.Black,
    clickedBackgroundColor: Color = Color.Gray,
    contentColor: Color = Color.White,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.dp,
    fontSize: TextUnit = 64.sp,
    delayMillis: Long = 100 // Time in milliseconds to switch back to initial color
) {
    var backgroundColor by remember { mutableStateOf(initialBackgroundColor) }
    var isClicked by remember { mutableStateOf(false) }

    LaunchedEffect(isClicked) {
        if (isClicked) {
            delay(delayMillis)
            backgroundColor = initialBackgroundColor
            isClicked = false
        }
    }

    Button(
        onClick = {
            backgroundColor = clickedBackgroundColor
            isClicked = true
            onClick()
        },
        modifier = modifier
            .width(width)
            .height(height)
            .border(borderWidth, borderColor, shape = ButtonDefaults.outlinedShape),
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

@Composable
fun CustomIconButton(
    iconId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    initialContainerColor: Color = Color.Black,
    clickedContainerColor: Color = Color.Gray,
    contentColor: Color = Color.White,
    iconSize: Dp = 150.dp,
    colorFilter: ColorFilter = ColorFilter.tint(Color.White),
    contentDescription: String = "",
    iconModifier: Modifier = Modifier,
    delayMillis: Long = 100
){
    var containerColor by remember { mutableStateOf(initialContainerColor) }
    var isClicked by remember { mutableStateOf(false) }

    LaunchedEffect(isClicked) {
        if (isClicked) {
            delay(delayMillis)
            containerColor = initialContainerColor
            isClicked = false
        }
    }

    Button(
        content = {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = contentDescription,
                modifier = iconModifier.size(iconSize),
                colorFilter = colorFilter
            )
        },
        onClick = {
            containerColor = clickedContainerColor
            isClicked = true
            onClick()
        },
        modifier = modifier
            .size(size)
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    )
}