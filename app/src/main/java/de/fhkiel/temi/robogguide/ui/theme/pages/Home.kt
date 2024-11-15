package de.fhkiel.temi.robogguide.ui.theme.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.Header

/**
 * Home: first page after setup and the initial page where you come back to after the tour is finished.
 * @param innerPadding: PaddingValues - the padding values.
 * @param navHostController: NavHostController - the navigation controller.
 * @param mRobot: Robot? - the robot.
 * @param tourManager: TourManager - the tour manager.
 */
@Composable
fun Home(
    innerPadding: PaddingValues,
    navHostController: NavHostController,
    mRobot: Robot?,
    tourManager: TourManager) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header(
            title = "Willkommen im Computermuseum!",
            modifier = Modifier.padding(16.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Header(
            title = "Du befindest dich in ${tourManager.selectedPlace?.name}!",
            fontSize = 64.sp,
            fontWeight = FontWeight.Normal
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)) {
            CustomButton(
                title = "Los gehts!",
                onClick = {
                    navHostController.navigate("guideSelector")
                    mRobot?.let {
                        if (mRobot.volume == 0) {
                            mRobot.volume = 5 // volume needs to be between 0 and 10
                            Log.d("Home", "Volume set to ${mRobot.volume}")
                        }
                    }
                }
            )
        }
    }
}
