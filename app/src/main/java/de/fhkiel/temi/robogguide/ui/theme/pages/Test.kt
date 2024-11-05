package de.fhkiel.temi.robogguide.ui.theme.pages

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.ui.theme.components.TextDisplay

@Composable
fun Test(mRobot: Robot?, tourManager: TourManager) {
    Column {
        tourManager.selectedPlace?.allLocations?.forEach { location: Location ->
            Log.d("Test", "Location: ${location.name}")
            location.conciseText?.let {
                TextDisplay(text = it)
            }
            location.detailedText?.let {
                TextDisplay(text = it)
            }
        }
    }
}