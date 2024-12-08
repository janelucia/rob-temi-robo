package de.fhkiel.temi.robogguide.ui.theme.components

/**
 * This file contains the ui component: button in all forms and variations.
 */

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

/**
 * CustomButton
 * - being used for all buttons with text to have a cohesive look.
 * @param title: String - the text displayed on the button.
 * @param onClick: () -> Unit - the action to be executed when the button is clicked.
 * @param modifier: Modifier - the modifier for the button (styling and such).
 * @param width: Dp - the width of the button.
 * @param height: Dp - the height of the button.
 * @param initialBackgroundColor: Color - the initial background color of the button.
 * @param clickedBackgroundColor: Color - the background color of the button when clicked.
 * @param contentColor: Color - the color of the text on the button.
 * @param borderColor: Color - the color of the border of the button.
 * @param borderWidth: Dp - the width of the border of the button.
 * @param fontSize: TextUnit - the size of the text on the button.
 * @param delayMillis: Long - the delay in milliseconds before the button returns to its initial state.
 */
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
    delayMillis: Long = 100,
    enabled: Boolean = true
) {
    var backgroundColor by remember { mutableStateOf(initialBackgroundColor) }
    var isClicked by remember { mutableStateOf(false) }

    // delay the return to the initial color of the button
    // used to indicate that the button has been clicked
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
        enabled = enabled
    ) {
        Text(
            text = title,
            fontSize = fontSize,
            lineHeight = fontSize,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * CustomIconButton
 * - being used for all buttons with icons to have a cohesive look.
 * @param iconId: Int - the id of the icon to be displayed on the button.
 * @param onClick: () -> Unit - the action to be executed when the button is clicked.
 * @param modifier: Modifier - the modifier for the button (styling and such).
 * @param size: Dp - the size of the button.
 * @param initialContainerColor: Color - the initial background color of the button.
 * @param clickedContainerColor: Color - the background color of the button when clicked.
 * @param contentColor: Color - the color of the icon on the button.
 * @param iconSize: Dp - the size of the icon on the button.
 * @param colorFilter: ColorFilter - the color filter of the icon on the button.
 * @param contentDescription: String - the content description of the icon on the button.
 * @param iconModifier: Modifier - the modifier for the icon (styling and such).
 * @param delayMillis: Long - the delay in milliseconds before the button returns to its initial state.
 */
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

    // delay the return to the initial color of the button
    // used to indicate that the button has been clicked
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