package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.logic.SetupViewModel
import de.fhkiel.temi.robogguide.ui.theme.components.LoadingSpinner
import de.fhkiel.temi.robogguide.ui.theme.components.SetupUi
import kotlinx.coroutines.delay

@Composable
fun Setup(setupViewModel: SetupViewModel, tourManager: TourManager) {

    var loading by remember { mutableStateOf(true) }
    var isDatabaseValid by remember { mutableStateOf(false) }
    var currentMessageIndex by remember { mutableStateOf(0) }

    val messages = listOf(
        "Checking database...",
        "Loading data...",
        "Setting up the map...",
        "Almost there...",
        "Cleaning up...",
        "Ready!"
    )

    LaunchedEffect(Unit) {
        isDatabaseValid = tourManager.allPlaces.isNotEmpty()
        loading = false
    }

    LaunchedEffect(Unit) {
        for (i in messages.indices) {
            delay(4000)
            currentMessageIndex = i
        }
    }

    if (loading) {
        LoadingSpinner(messages = messages, currentMessageIndex = currentMessageIndex)
    } else {
        if (isDatabaseValid) {
            SetupUi(tourManager = tourManager, setupViewModel = setupViewModel)
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Datenbank ist fehlerhaft!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}