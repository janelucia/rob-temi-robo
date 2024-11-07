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
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxSize()
            ) {
                    Text("Wie kann ich dir helfen?", style = MaterialTheme.typography.headlineLarge)

                    CustomButton(
                        onClick = {
                            exitApp(activity)
                        },
                        title = "App schließen",
                        width = 400.dp,
                        height = 100.dp,
                        fontSize = 32.sp,
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                    )

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
