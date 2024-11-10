package de.fhkiel.temi.robogguide.ui.theme.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.MainActivity
import de.fhkiel.temi.robogguide.R
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    navController: NavController,
    tourViewModel: TourViewModel,
    activity: Activity,
    mRobot: Robot?
) {
    var showHelpPopup by remember { mutableStateOf(false) }
    var showPopUp by remember { mutableStateOf(false) }
    var showConfirmationPopUp by remember { mutableStateOf(false) }
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route
    val currentLocationIndex by tourViewModel.currentLocationIndex.observeAsState(0)

    if (showHelpPopup) {
        HelpPopup(onDismiss = { showHelpPopup = false }, activity)
    }

    if (showPopUp) {
        ClosePopup(onDismiss = { showPopUp = false }, navController, mRobot)
    }

    if (showConfirmationPopUp) {
        ConfirmationPopUp(
            onDismiss = {
                showConfirmationPopUp = false
            },
            onConfirm = {
                robotSpeakText(mRobot, "Ich fahre jetzt zur Aufladestation!", false)
                mRobot?.goTo("home base")
                showConfirmationPopUp = false
            },
            title = "Roboter zur Ladestation fahren lassen",
            message = "Möchtest du den Roboter wirklich zur Ladestation fahren lassen?",
            confirmationButtonText = "Zur Ladestation",
            dismissButtonText = "Abbrechen"
        )
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.fachhochschule_kiel_logo_03_2022),
                        contentDescription = "Fachhochschule Kiel Logo",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            },
            actions = {
                if (currentDestination != "homePage") {
                    IconButton(
                        onClick = {
                            if (currentDestination == "guide") {
                                showPopUp = true
                            } else {
                                navController.navigate("homePage")
                            }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .border(2.dp, Color.Black, CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.Black
                        )
                    }
                }
                if (currentDestination == "homePage") {
                    CustomButton(
                        title = "Roboter zur Ladestation schicken",
                        onClick = {
                            showConfirmationPopUp = true
                        },
                        width = 600.dp,
                        height = 55.dp,
                        fontSize = 32.sp,
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black,
                        borderWidth = 2.dp,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "?",
                    onClick = {
                        showHelpPopup = true
                    },
                    modifier = Modifier.size(50.dp),
                    fontSize = 24.sp,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (currentDestination == "guide") {
            Header(
                title = "Station ${currentLocationIndex + 1} von ${tourViewModel.numberOfLocations}: ${tourViewModel.giveCurrentLocation().name}",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.Center) // Header mittig auf dem GESAMTEN BILDSCHIRM platzieren
                    .padding(top = 8.dp)

            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupTopBar(activity: Activity) {
    var showPopUp by remember { mutableStateOf(false) }
    var showConfirmationPopUp by remember { mutableStateOf(false) }

    if (showPopUp) {
        PreparationPopUp(onDismiss = { showPopUp = false })
    }

    if (showConfirmationPopUp) {
        ConfirmationPopUp(
            onDismiss = { showConfirmationPopUp = false },
            onConfirm = { exitApp(activity) },
            title = "App schließen",
            message = "Möchtest du die App wirklich schließen?",
            confirmationButtonText = "App schließen",
            dismissButtonText = "Abbrechen"
        )
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Text(
                    "Gruppe Pentagon - Temi Setup",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            actions = {
                CustomButton(
                    title = "Vorbereitungen",
                    onClick = {
                        showPopUp = true
                    },
                    width = 300.dp,
                    height = 55.dp,
                    fontSize = 32.sp,
                    initialBackgroundColor = Color.White,
                    contentColor = Color.Black,
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "App schließen",
                    onClick = {
                        showConfirmationPopUp = true
                    },
                    width = 300.dp,
                    height = 55.dp,
                    fontSize = 32.sp,
                )
            }
        )
    }
}

fun exitApp(activity: Activity = MainActivity()) {
    activity.finishAndRemoveTask()
}
