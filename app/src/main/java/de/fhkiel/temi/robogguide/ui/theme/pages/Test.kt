package de.fhkiel.temi.robogguide.ui.theme.pages

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.theme.components.TextDisplay

@Composable
fun Test(mRobot: Robot?, tourManager: TourManager) {
    tourManager.currentPlace?.let {
        Log.d("Test", it.name)
        Column {
            it.getAllLocations().forEach { location ->
                Log.d("Test", location.name)
            }
            it.getAllLocations().first().conciseText?.let { text ->
                TextDisplay(text = text)
            }
            it.getAllLocations().first().detailedText?.let { text ->
                TextDisplay(text = text)
            }
        }
    }

}