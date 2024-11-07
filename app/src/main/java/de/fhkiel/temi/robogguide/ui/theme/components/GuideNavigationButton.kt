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
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel

@Composable
fun GuideNavigationButton(
    navController: NavController,
    tourManager: TourManager,
    tourViewModel: TourViewModel,
    mRobot: Robot?
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    val numberOfItems by tourViewModel.numberOfItemsAtCurrentLocation.observeAsState(0)
    val currentItemIndex by tourViewModel.currentItemIndex.observeAsState(0)
    val currentItem by tourViewModel.currentItem.observeAsState(null)

    val wasAlreadySpoken by tourViewModel.wasAlreadySpoken.observeAsState(false)


    if (currentDestination == "guide") {
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                GuideProgressBar(numberOfItems, currentItemIndex)
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
                            tourViewModel.decrementCurrentItemIndex()
                        }
                    )
                    Header(title = "⟲",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            if (wasAlreadySpoken) {
                                assert(tourViewModel.levelOfDetail != null)
                                if (tourViewModel.levelOfDetail?.isDetailed() == true) {
                                    val text =
                                        currentItem?.conciseText?.value + "\n" + currentItem?.detailedText?.value
                                    robotSpeakText(mRobot, text)
                                } else {
                                    robotSpeakText(
                                        mRobot,
                                        currentItem?.conciseText?.value
                                    )
                                }
                            } else {
                                // don't speak
                                Log.w("GuideNavigationButton", "Stop spamming me!")
                            }
                        }


                    )
                    Header(title = "⏭",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            tourViewModel.updateCurrentItem(currentItemIndex + 1)
                        }
                    )
                    /*Icon(imageVector = Icons.Filled.Refresh,
                        contentDescription = "Play text again",
                        tint = Color.Black)*/
                }
            }
        }


    }
}