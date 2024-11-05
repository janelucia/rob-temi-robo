package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.logic.SetupViewModel
import de.fhkiel.temi.robogguide.ui.theme.components.LoadingSpinner
import de.fhkiel.temi.robogguide.ui.theme.components.SetupUi
import kotlinx.coroutines.delay

@Composable
fun Setup(setupViewModel: SetupViewModel, tourManager: TourManager, hasError: Boolean) {

    var loading by remember { mutableStateOf(true) }
    var isDatabaseValid by remember { mutableStateOf(false) }
    var currentMessageIndex by remember { mutableIntStateOf(0) }

    val messages = listOf(
        "Checking database...",
        "Loading data...",
        "Setting up the map...",
        "Almost there...",
        "Cleaning up...",
        "Ready!"
    )

    LaunchedEffect(Unit) {
        isDatabaseValid = tourManager.allPlacesMap.isNotEmpty()
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
        if (hasError) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Error: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 64.sp,
                        style = TextStyle(color = Color.Red)
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        text = tourManager.error?.message ?: "unbekannter Fehler",
                        fontSize = 32.sp,
                    )
                }
            }
        } else if (isDatabaseValid) {
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