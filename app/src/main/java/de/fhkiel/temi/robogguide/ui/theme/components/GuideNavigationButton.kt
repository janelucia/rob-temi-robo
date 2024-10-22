package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun GuideNavigationButton(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    if (currentDestination == "guide") {
        // Navigations-Buttons für Exponate
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomButton(
                title = "⏮",
                width = 100.dp,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                onClick = { /* zurück zum vorherigen exponat gehen */ }
            )
            CustomButton(
                title = "\uD83D\uDD04",
                width = 100.dp,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                onClick = { /* aktuellen text nochmal ausgeben */ }
            )
            CustomButton(
                title = "⏭",
                width = 100.dp,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                onClick = { /* direkt zum nächsten exponat gehen */ }
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            Header(
                title = "Zurück zu {exponat}",
                fontSize = 16.sp
            )
            Header(
                title = "Ausgabe wiederholen",
                fontSize = 16.sp
            )
            Header(
                title = "Weiter zu {exponat}",
                fontSize = 16.sp
            )
        }
    }
}