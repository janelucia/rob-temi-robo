package de.fhkiel.temi.robogguide.ui.theme.pages

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.ui.theme.components.LocationPreview

@Composable
fun Test(innerPadding: PaddingValues, mRobot: Robot?, tourManager: TourManager) {
    // Create ScrollState to own it and be able to control scroll behaviour of scrollable Row below
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
    ) {
        tourManager.selectedPlace?.allLocations?.forEach { location: Location ->
            Log.d("Test", "Location: ${location.name}")
            LocationPreview(innerPadding, location = location, mRobot)
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}