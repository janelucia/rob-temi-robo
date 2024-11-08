package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.robotemi.sdk.Robot
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

@Composable
fun ClosePopup(onDismiss: () -> Unit, navController: NavController, mRobot: Robot?) {
    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
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
                                robot?.goTo("home base")
                            }
                        },
                        title = "Tour beenden und Roboter zurück zur Ladestation schicken",
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