package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.models.GuideState
import de.fhkiel.temi.robogguide.ui.logic.SetupViewModel
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.Exhibit
import de.fhkiel.temi.robogguide.ui.theme.components.TransferDrive

/**
 * Guide: the guide page.
 * @param innerPadding: PaddingValues - the padding values.
 * @param mRobot: Robot? - the robot.
 * @param tourViewModel: TourViewModel - the view model for the tour.
 * @param tourManager: TourManager - the tour manager.
 * @param navController: NavController - the navigation controller.
 * @param setupUiViewModel: SetupViewModel - the view model for the setup. Needed to display the debug flag.
 */
@Composable
fun Guide(
    innerPadding: PaddingValues,
    mRobot: Robot?,
    tourViewModel: TourViewModel,
    tourManager: TourManager,
    navController: NavController,
    setupUiViewModel: SetupViewModel
) {

    val guideState by tourViewModel.guideState.observeAsState(null)
    val currentItem by tourViewModel.currentItem.observeAsState(null)
    val currentLocation by tourViewModel.currentLocation.observeAsState(null)
    val isDebugFlagEnabled by setupUiViewModel.isDebugFlagEnabled.observeAsState(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (guideState) {
            null -> {
                if (currentLocation != null) {
                    tourViewModel.updateGuideState(GuideState.TransferStart)
                }
            }

            GuideState.TransferStart -> {
                assert(currentLocation != null)
                TransferDrive(currentLocation!!, mRobot, tourViewModel, tourManager, navController)
                if (isDebugFlagEnabled) {
                    CustomButton(
                        title = "Am Exponat angekommen",
                        onClick = { tourViewModel.updateGuideState(GuideState.Exhibit) }
                    )
                }
            }

            GuideState.Exhibit -> {
                assert(currentItem != null)
                Exhibit(currentItem!!, mRobot, tourViewModel)
                if (isDebugFlagEnabled) {
                    CustomButton(
                        title = "Zum nächsten Exponat",
                        onClick = {
                            tourViewModel.updateGuideState(GuideState.TransferStart)
                            tourViewModel.incrementCurrentItemIndex()
                        }
                    )
                }
            }

            GuideState.TransferGoing -> {
                assert(currentLocation != null)
                TransferDrive(currentLocation!!, mRobot, tourViewModel, tourManager, navController)
            }

            GuideState.End -> {
                //do nothing
            }

            GuideState.TransferError -> {
                assert(currentLocation != null)
                TransferDrive(currentLocation!!, mRobot, tourViewModel, tourManager, navController)
            }
            GuideState.TransferAbort -> {
                assert(currentLocation != null)
                TransferDrive(currentLocation!!, mRobot, tourViewModel, tourManager, navController)
            }
        }
    }

}
