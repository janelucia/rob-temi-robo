package de.fhkiel.temi.robogguide.ui.theme.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

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
                    Text("Wie darf ich dir helfen?",
                        fontSize = 64.sp,
                        lineHeight = 64.sp)
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

@Composable
fun PreparationPopUp(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Header("Vorbereitungen",
                    fontSize = 64.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Text("Schalte den Kioskmodus ein. Du findest einen Schalter auf der Seite: Temi-Setup.",
                    fontSize = 24.sp,
                    lineHeight = 32.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Stelle sicher, dass Temi den User tracken kann. Dies kannst du unter Einstellungen -> General Settings -> Andere -> Tracking User einschalten.",
                    fontSize = 24.sp,
                    lineHeight = 32.sp)
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

@Composable
fun ConfirmationPopUp(onDismiss: () -> Unit, onConfirm: () -> Unit, title: String, message: String, confirmationButtonText: String, dismissButtonText: String) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Header(title,
                    fontSize = 64.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Text(message,
                    fontSize = 24.sp,
                    lineHeight = 32.sp)
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
