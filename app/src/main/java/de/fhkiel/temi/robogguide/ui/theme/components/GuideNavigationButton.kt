package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel

@Composable
fun GuideNavigationButton(
    navController: NavController,
    tourViewModel: TourViewModel,
    mRobot: Robot?
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    val numberOfItems by tourViewModel.numberOfItemsAtCurrentLocation.observeAsState(0)
    val currentItemIndex by tourViewModel.currentItemIndex.observeAsState(0)
    val currentItem by tourViewModel.currentItem.observeAsState(null)
    val currentLocationIndex by tourViewModel.currentLocationIndex.observeAsState(0)

    val wasAlreadySpoken by tourViewModel.wasAlreadySpoken.observeAsState(false)


    if (currentDestination == "guide") {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(200.dp),
            ) {
                GuideProgressBar(numberOfItems, currentItemIndex)
            }


            Box {
                // Navigations-Buttons für Exponate
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomButton(
                        title = "⏮",
                        fontSize = 32.sp,
                        onClick = {
                            tourViewModel.decrementCurrentItemIndex()
                        },
                        modifier = Modifier
                            .size(150.dp)
                            .padding(16.dp),
                    )
                    CustomButton(title = "⟲",
                        fontSize = 32.sp,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(16.dp),
                        onClick = {
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
                    if (currentItemIndex == numberOfItems - 1 && currentLocationIndex == tourViewModel.numberOfLocations - 1) {
                        CustomButton(
                            title = "Führung beenden",
                            fontSize = 32.sp,
                            onClick = {
                                navController.navigate("endPage")
                            },
                            modifier = Modifier.padding(16.dp),
                            height = 100.dp,
                            width = 300.dp
                        )
                    } else {
                        CustomButton(title = "⏭",
                            fontSize = 32.sp,
                            modifier = Modifier
                                .size(150.dp)
                                .padding(16.dp),
                            onClick = {
                                tourViewModel.incrementCurrentItemIndex()
                            }
                        )
                    }
                }
            }
        }


    }
}