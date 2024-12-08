package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalDensity

/**
 * GuideProgressBar
 * - Displays a progress bar with 'beads' for each exhibit of the current location
 * @param numberOfExhibits: Number of exhibits in the guide
 * @param currentExhibit: Index of the current exhibit
 */
@Composable
fun GuideProgressBar(numberOfExhibits: Int, currentExhibit: Int) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    LaunchedEffect(currentExhibit) {
        val rowHeight = with(density) { 32.dp.toPx() } // Height of each row including padding
        val targetScroll = ((currentExhibit / 5) * rowHeight).toInt()

        coroutineScope.launch {
            scrollState.animateScrollTo(targetScroll)
        }
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp) // Set the desired height
            .verticalScroll(scrollState)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Header(title = "Ausstellungsstück(e):", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            val rows = (numberOfExhibits + 4) / 5 // Calculate the number of rows needed
            for (row in 0 until rows) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val start = row * 5
                    val end = minOf(start + 5, numberOfExhibits)
                    for (i in start until end) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(2.dp, Color.Black, shape = ButtonDefaults.outlinedShape)
                                .background(
                                    if (i == currentExhibit) Color.Black else Color.White,
                                    CircleShape
                                )
                                .padding(4.dp)
                        )
                        if (i < end - 1) {
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height(2.dp)
                                    .background(Color.Gray)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}