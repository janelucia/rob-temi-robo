package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel

@Composable
fun GuideNavigationButton(
    navController: NavController,
    tourManager: TourManager,
    tourViewModel: TourViewModel
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    val numberOfItems by tourViewModel.numberOfItemsAtCurrentLocation.observeAsState(0)
    val currentItem by tourViewModel.currentItemIndex.observeAsState(0)

    if (currentDestination == "guide") {
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                GuideProgressBar(numberOfItems, currentItem)
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
                        modifier = Modifier.clickable {
                            tourViewModel.updateCurrentItem(currentItem - 1)
                            Log.d("Test", "currentItem -1: ${currentItem} number of loc: ${numberOfItems}")
                            //TODO robo und ui stuff
                        }
                    )
                    Header(title = "⟲",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* aktuellen text nochmal ausgeben */ }
                    )
                    Header(title = "⏭",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            tourViewModel.updateCurrentItem(currentItem + 1)
                            Log.d("Test", "currentExhibit +1: ${currentItem}")
                            //TODO robo und ui stuff
                        }
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