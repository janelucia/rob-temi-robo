package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ClosePopup(onDismiss: () -> Unit, navController: NavController) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Möchtest du die Tour wirklich beenden?",
                fontSize = 32.sp)
        },
        text = {
            Text("Dadurch wird die Tour beendet und du wirst zum Startbildschirm zurückgeleitet.",
                fontSize = 24.sp)
        },
        confirmButton = {
            CustomButton(
                onClick = {
                    navController.navigate("homePage")
                    onDismiss()
                },
                title = "Zum Startbildschirm",
                modifier = Modifier.fillMaxWidth(),
                height = 100.dp,
                fontSize = 24.sp,
                backgroundColor = Color.White,
                contentColor = Color.Black
            )
        },
        dismissButton = {
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
    )
}