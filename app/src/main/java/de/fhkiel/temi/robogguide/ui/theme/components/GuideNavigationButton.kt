package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun GuideNavigationButton(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    //todo ändern zu Zugriff auf DB/vorgeladene Strukturen
    var numberOfExhibits = 10
    var currentExhibit = 3

    if (currentDestination == "guide") {
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                GuideProgressBar(numberOfExhibits, currentExhibit)
            }


            Box() {
                // Navigations-Buttons für Exponate
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Header(title = "⏮",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* zurück zum vorherigen exponat gehen */ }
                    )
                    Header(title = "⟲",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* aktuellen text nochmal ausgeben */ }
                    )
                    Header(title = "⏭",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* direkt zum nächsten exponat gehen */ }
                    )
                    /*Icon(imageVector = Icons.Filled.Refresh,
                        contentDescription = "Play text again",
                        tint = Color.Black)*/
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


    }
}