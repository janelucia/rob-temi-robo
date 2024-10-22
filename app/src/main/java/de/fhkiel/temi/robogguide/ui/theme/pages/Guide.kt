package com.example.rob_temi_robo_ui.ui.theme.pages

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.Header
import de.fhkiel.temi.robogguide.R
import de.fhkiel.temi.robogguide.ui.theme.components.GuideProgressBar

@Composable
fun Guide(innerPadding: PaddingValues, navHostController: NavHostController, mRobot: Robot?) {

    var guideState by remember { mutableStateOf(GuideState.Transition) }
    var showMenu by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (guideState) {
            GuideState.Transition -> {
                Header(
                    title = "Kurze Führung (30 Minuten)",
                    fontSize = 32.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Header(
                    title = "Bitte folgen Sie mir!",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Header(
                    title = "Das nächste Exponat ist:",
                    fontSize = 32.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.computermuseum_maerz_23),
                    contentDescription = "Example Image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                //TODO aktuell noch Button oder Timer, um die nächste Phase zu triggern (Wechsel zur Exponat-Sequenz)
                CustomButton(
                    title = "Am Exponat angekommen",
                    onClick = { guideState = GuideState.Exhibit }
                )
            }

            GuideState.Exhibit -> {
                Header(
                    title = "Exponat: Ein super mega toller Computer",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Header(
                    title = "Hier ist ganz viel Inhalt:",
                    fontSize = 32.sp
                )
                Header(
                    title = "Irgendein Text Inhalt",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Optional: Bild oder weitere Details
                Image(
                    painter = painterResource(id = R.drawable.computermuseum_maerz_23),
                    contentDescription = "Exponat Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //TODO aktuell noch Button oder Mechanismus, um zur nächsten Übergangssequenz zu wechseln
                    CustomButton(
                        title = "Zum nächsten Exponat",
                        onClick = { guideState = GuideState.Transition }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomButton(
                        title = "Menü",
                        onClick = { showMenu = true }
                    )
                }

            }
        }
    }
    if (showMenu) {
        Dialog(
            onDismissRequest = { showMenu = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            GuideProgressBar()
        }

    }

}


enum class GuideState {
    Transition, // robot leads guest to next exhibit
    Exhibit     // robot is at exhibit
    //TODO vielleicht weitere states hinzufügen wie exhibit introduction, exhibit idle, exhibit end (...)
}