package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.R
import de.fhkiel.temi.robogguide.logic.clearQueue
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.models.GuideState
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel

/**
 * GuideNavigationButton
 * - shows the navigation buttons for the guide and the detailed exhibit views
 * @param navController: NavController
 * @param tourViewModel: TourViewModel
 * @param mRobot: Robot?
 */
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
    val currentGuideState by tourViewModel.guideState.observeAsState(GuideState.TransferStart)

    when (currentDestination) {
        // navigation buttons for guide
        "guide" -> {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GuideProgressBar(numberOfItems, currentItemIndex)

                Box {
                    // navigation buttons for items
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // dont show previous button if at the beginning
                        if (currentItemIndex == 0 && currentLocationIndex == 0) {
                            CustomIconButton(
                                iconId = R.drawable.play_disabled_right,
                                onClick = {
                                    robotSpeakText(
                                        mRobot,
                                        "Das geht leider nicht. Wir sind gerade am Anfang.",
                                        clearQueue = true
                                    )
                                },
                                contentDescription = "Kein vorheriges Exponat",
                                initialContainerColor = Color.Gray,
                                iconModifier = Modifier.graphicsLayer(rotationZ = 180f)
                            )
                        } else {
                            if (currentGuideState == GuideState.Exhibit && currentItemIndex != 0) {
                                CustomIconButton(
                                    iconId = R.drawable.play_arrow_left,
                                    onClick = {
                                        tourViewModel.decrementCurrentItemIndex()
                                        clearQueue(mRobot)
                                    },
                                    contentDescription = "Vorheriges Exponat"
                                )
                            } else {
                                CustomIconButton(
                                    iconId = R.drawable.skip_previous,
                                    onClick = {
                                        if (currentGuideState == GuideState.TransferGoing) {
                                            mRobot?.stopMovement()
                                        }
                                        tourViewModel.decrementCurrentLocationIndex()
                                        clearQueue(mRobot)
                                    },
                                    contentDescription = "Vorherige Station"
                                )
                            }

                        }
                        // if navigation threw an error the button allows the user to do a retry
                        if (currentGuideState == GuideState.TransferError || currentGuideState == GuideState.TransferAbort) {
                            CustomButton(
                                title = "Erneut versuchen",
                                fontSize = 32.sp,
                                onClick = {
                                    tourViewModel.updateGuideState(GuideState.TransferStart)
                                },
                                modifier = Modifier.padding(16.dp),
                                height = 100.dp,
                                width = 300.dp
                            )
                        }
                        // if the guide is in the exhibit state the buttons are different
                        // prevents the user from repeat the item if he isn't at the exhibit state
                        if (currentGuideState == GuideState.Exhibit) {
                            CustomIconButton(
                                iconId = R.drawable.replay,
                                contentDescription = "Exponat wiederholen",
                                onClick = {
                                    // wait to speak
                                    if (wasAlreadySpoken) {
                                        assert(tourViewModel.levelOfDetail != null)
                                        // first speak the concise text
                                        robotSpeakText(
                                            mRobot,
                                            currentItem?.conciseText?.value,
                                            clearQueue = true
                                        )
                                        // if the level of detail is detailed also speak the detailed text
                                        if (tourViewModel.levelOfDetail?.isDetailed() == true) {
                                            robotSpeakText(
                                                mRobot,
                                                currentItem?.detailedText?.value
                                            )
                                        }
                                    } else {
                                        // don't speak
                                        Log.w("GuideNavigationButton", "Stop spamming me!")
                                    }
                                },
                            )
                            CustomIconButton(
                                iconId = R.drawable.stop,
                                contentDescription = "Sprachausgabe stoppen",
                                onClick = {
                                    clearQueue(mRobot)
                                },
                            )

                        }
                        // allow to end guide if at the last item and location or if the guide is at the last location not at an Exhibit
                        if (currentLocationIndex == tourViewModel.numberOfLocations - 1 &&
                            (currentItemIndex == numberOfItems - 1 || currentGuideState != GuideState.Exhibit)
                        ) {
                            CustomButton(
                                title = "Führung beenden",
                                fontSize = 32.sp,
                                onClick = {
                                    navController.navigate("endPage")
                                    clearQueue(mRobot)
                                },
                                modifier = Modifier.padding(16.dp),
                                height = 100.dp,
                                width = 300.dp
                            )
                        } else {
                            if (currentGuideState == GuideState.Exhibit && currentItemIndex != numberOfItems - 1) {
                                CustomIconButton(
                                    iconId = R.drawable.play_arrow_right,
                                    contentDescription = "Nächstes Exponat",
                                    onClick = {
                                        tourViewModel.incrementCurrentItemIndex()
                                        clearQueue(mRobot)
                                    },
                                )
                            } else {
                                CustomIconButton(
                                    iconId = R.drawable.skip_next,
                                    contentDescription = "Nächste Station",
                                    onClick = {
                                        if (currentGuideState == GuideState.TransferGoing) {
                                            mRobot?.stopMovement()
                                        }
                                        tourViewModel.incrementCurrentLocationIndex()
                                        clearQueue(mRobot)
                                    },
                                )
                            }
                        }
                    }
                }
            }

        }

        // navigation buttons for items
        "detailedExhibit" -> {
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
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // show button to retry if an error occurred
                        if (currentGuideState == GuideState.TransferError) {
                            CustomButton(
                                title = "Erneut versuchen",
                                fontSize = 32.sp,
                                onClick = {
                                    tourViewModel.updateGuideState(GuideState.TransferStart)
                                },
                                modifier = Modifier.padding(16.dp),
                                height = 100.dp,
                                width = 300.dp
                            )
                        }
                        // show navigation buttons only for items
                        if (currentGuideState == GuideState.Exhibit) {
                            // show disabled previous button if at the first item
                            if (currentItemIndex == 0) {
                                CustomIconButton(
                                    iconId = R.drawable.play_disabled_right,
                                    onClick = {
                                        robotSpeakText(
                                            mRobot,
                                            "Das geht leider nicht. Wir sind gerade am Anfang.",
                                            clearQueue = true
                                        )
                                    },
                                    contentDescription = "Kein vorheriges Exponat",
                                    initialContainerColor = Color.Gray,
                                    iconModifier = Modifier.graphicsLayer(rotationZ = 180f)
                                )
                            } else {
                                CustomIconButton(
                                    iconId = R.drawable.play_arrow_left,
                                    onClick = {
                                        tourViewModel.decrementCurrentItemIndex()
                                        clearQueue(mRobot)
                                    },
                                    contentDescription = "Vorheriges Exponat"
                                )
                            }
                            CustomIconButton(
                                iconId = R.drawable.replay,
                                contentDescription = "Exponat wiederholen",
                                onClick = {
                                    // wait to speak
                                    if (wasAlreadySpoken) {
                                        assert(tourViewModel.levelOfDetail != null)
                                        robotSpeakText(
                                            mRobot,
                                            currentItem?.conciseText?.value,
                                            clearQueue = true
                                        )
                                        if (tourViewModel.levelOfDetail?.isDetailed() == true) {
                                            robotSpeakText(
                                                mRobot,
                                                currentItem?.detailedText?.value
                                            )
                                        }
                                    } else {
                                        // don't speak
                                        Log.w("GuideNavigationButton", "Stop spamming me!")
                                    }
                                }
                            )
                            CustomIconButton(
                                iconId = R.drawable.stop,
                                contentDescription = "Sprachausgabe stoppen",
                                onClick = {
                                    clearQueue(mRobot)
                                },
                            )
                            // show disabled next button if at the last item
                            if (currentItemIndex == numberOfItems - 1) {
                                CustomIconButton(
                                    iconId = R.drawable.play_disabled_right,
                                    onClick = {
                                        robotSpeakText(
                                            mRobot,
                                            "Das geht leider nicht. Das war das letzte Exponat",
                                            clearQueue = true
                                        )
                                    },
                                    contentDescription = "Kein vorheriges Exponat",
                                    initialContainerColor = Color.Gray,
                                )
                            } else {
                                CustomIconButton(
                                    iconId = R.drawable.play_arrow_right,
                                    contentDescription = "Nächstes Exponat",
                                    onClick = {
                                        tourViewModel.incrementCurrentItemIndex()
                                        clearQueue(mRobot)
                                    },
                                )
                            }
                        }
                        CustomButton(
                            title = "Zurück zur Liste",
                            fontSize = 32.sp,
                            onClick = {
                                navController.popBackStack()
                                mRobot?.stopMovement()
                                clearQueue(mRobot)
                            },
                            modifier = Modifier.padding(16.dp),
                            height = 100.dp,
                            width = 300.dp
                        )
                    }
                }
            }
        }
    }
}