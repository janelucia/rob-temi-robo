package de.fhkiel.temi.robogguide.ui.theme.components

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.clearQueue
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.ui.logic.SetupViewModel

/**
 * PopUp
 * contains different PopUps for different purposes
 * - HelpPopup: Popup to ask for help
 * - PreparationPopUp: Popup to show the user the necessary preparations to start the temi
 * - ConfirmationPopUp: Popup to confirm an action
 * - ClosePopup: Popup to ask the user if he wants to end the tour
 * - ErrorPopUp: Popup to show an error message
 */

/**
 * HelpPopup: Popup, can currently only close the app.
 * - future: add more options like: call help desk of computer museum
 * @param onDismiss: function to dismiss the popup
 * @param activity: current activity
 */
@Composable
fun HelpPopup(onDismiss: () -> Unit, activity: Activity) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Wie darf ich dir helfen?",
                    fontSize = 64.sp,
                    lineHeight = 64.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    onClick = {
                        exitApp(activity)
                    },
                    title = "App schließen",
                    width = 400.dp,
                    height = 100.dp,
                    fontSize = 32.sp,
                    initialBackgroundColor = Color.White,
                    contentColor = Color.Black,
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    onClick = onDismiss,
                    title = "Abbrechen",
                    width = 400.dp,
                    height = 100.dp,
                    fontSize = 32.sp
                )
            }
        }
    }
}

/**
 * PreparationPopUp: Popup to show the user the necessary preparations to start the temi
 * @param onDismiss: function to dismiss the popup
 * @param isDebugFlagEnabled: boolean to enable debug mode
 * @param setupViewModel: viewmodel to handle the debug flag
 */
@Composable
fun PreparationPopUp(
    onDismiss: () -> Unit,
    isDebugFlagEnabled: Boolean,
    setupViewModel: SetupViewModel
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Header(
                    "Vorbereitungen",
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    "Schalte den Kioskmodus ein. Du findest einen Schalter auf der Seite: Temi-Setup.",
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Stelle sicher, dass Temi den User tracken kann. Dies kannst du unter Einstellungen -> General Settings -> Andere -> Tracking User einschalten.",
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "In den Temi Einstellungen -> Navigation -> Clear Way TTS ausschalten.",
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    "In den Temi Einstellungen -> Navigation -> Bypass Obstacle einschalten.",
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    "In den Temi Einstellungen -> General Settings -> unter Benachrichtigungen alles ausschalten.",
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text("Debug Flag anmachen")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isDebugFlagEnabled,
                    onCheckedChange = { enabled: Boolean ->
                        setupViewModel.setDebugFlagEnabled(enabled)
                        Log.i("de.fhkiel.temi.robogguide.ui.theme.components.SetupUi", "Debug mode: $enabled")
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                CustomButton(
                    onClick = onDismiss,
                    title = "Schließen",
                    width = 400.dp,
                    height = 50.dp,
                    fontSize = 32.sp
                )
            }
        }
    }
}

/**
 * ConfirmationPopUp: Popup to confirm an action like sending the robot to the charging station
 * @param onDismiss: function to dismiss the popup
 * @param onConfirm: function to confirm the action
 * @param title: title of the popup
 * @param message: message of the popup
 * @param confirmationButtonText: text of the confirmation button
 * @param dismissButtonText: text of the dismiss button
 */
@Composable
fun ConfirmationPopUp(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmationButtonText: String,
    dismissButtonText: String
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Header(
                    title,
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    message,
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    onClick = onConfirm,
                    title = confirmationButtonText,
                    width = 400.dp,
                    height = 50.dp,
                    fontSize = 32.sp,
                    initialBackgroundColor = Color.White,
                    contentColor = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    onClick = onDismiss,
                    title = dismissButtonText,
                    width = 400.dp,
                    height = 50.dp,
                    fontSize = 32.sp
                )
            }
        }
    }
}

/**
 * ClosePopup: Popup to ask the user if he wants to end the tour
 * @param onDismiss: function to dismiss the popup
 * @param navController: navigation controller to navigate to the home page
 * @param mRobot: robot object to stop the movement
 */
@Composable
fun ClosePopup(
    onDismiss: () -> Unit,
    navController: NavController,
    mRobot: Robot?) {
    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Möchtest du die Tour wirklich beenden?",
                        fontSize = 32.sp,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    Text(
                        "Dadurch wird die Tour beendet und du wirst zum Startbildschirm zurückgeleitet.",
                        fontSize = 24.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    CustomButton(
                        onClick = {
                            navController.navigate("homePage")
                            clearQueue(mRobot)
                            mRobot?.stopMovement()
                            onDismiss()
                        },
                        title = "Zum Startbildschirm",
                        modifier = Modifier.fillMaxWidth(),
                        height = 100.dp,
                        fontSize = 24.sp,
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    CustomButton(
                        onClick = {
                            onDismiss()
                            mRobot.let { robot ->
                                robotSpeakText(mRobot, "Ich fahre jetzt zur Aufladestation!")
                                navController.navigate("homePage")
                                robot?.goTo("home base")
                            }
                        },
                        title = "Roboter zurück zur Ladestation schicken",
                        modifier = Modifier.fillMaxWidth(),
                        height = 100.dp,
                        fontSize = 24.sp,
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    CustomButton(
                        onClick = {
                            onDismiss()
                        },
                        title = "Tour fortsetzen",
                        modifier = Modifier.fillMaxWidth(),
                        height = 100.dp,
                        fontSize = 24.sp
                    )
                }
            }
        }
    )
}

/**
 * ErrorPopUp: Popup to show an error message
 * @param onDismiss: function to dismiss the popup
 * @param title: title of the popup
 * @param message: message of the popup
 * @param onClick: function to try again
 * @param navController: navigation controller to navigate to the home page
 * @param mRobot: robot object to send the robot to the charging station
 * @param chargingStation: boolean to show the option to send the robot to the charging station - default true
 */
@Composable
fun ErrorPopUp(
    onDismiss: () -> Unit,
    title: String,
    message: String,
    onClick: () -> Unit,
    navController: NavController?,
    mRobot: Robot?,
    chargingStation: Boolean = true
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Header(
                    title,
                    fontSize = 32.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    message,
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    // only show the option to send the robot to the charging station if the chargingStation flag is true
                    // makes this error popup reusable for different purposes
                    if(chargingStation){
                        CustomButton(
                            onClick = {
                                onDismiss()
                                onClick()
                            },
                            title = "Erneut versuchen",
                            width = 400.dp,
                            height = 50.dp,
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        CustomButton(
                            onClick = {
                                onDismiss()
                                navController?.navigate("homePage")
                                robotSpeakText(
                                    mRobot,
                                    "Nagut, dann fahre ich erstmal wieder zurück zu meiner Ladestation. Bitte entschuldigen Sie.",
                                    false
                                )
                                mRobot?.goTo("home base")
                            },
                            title = "Zur Ladestation fahren",
                            width = 400.dp,
                            height = 50.dp,
                            fontSize = 32.sp,
                            initialBackgroundColor = Color.White,
                            contentColor = Color.Black
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    CustomButton(
                        onClick = onDismiss,
                        title = "Schließen",
                        width = 400.dp,
                        height = 50.dp,
                        fontSize = 32.sp
                    )
                }
            }
        }
    }
}
