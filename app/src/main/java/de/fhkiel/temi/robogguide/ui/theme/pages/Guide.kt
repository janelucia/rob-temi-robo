package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.models.GuideState
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.Exhibit
import de.fhkiel.temi.robogguide.ui.theme.components.TransferDrive

@Composable
fun Guide(
    innerPadding: PaddingValues,
    navHostController: NavHostController,
    mRobot: Robot?,
    tourManager: TourManager,
    tourViewModel: TourViewModel
) {

    val guideState by tourViewModel.guideState.observeAsState(null)

    val currentItem by tourViewModel.currentItem.observeAsState(null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (guideState) {
            null -> {
                if (currentItem != null) {
                    tourViewModel.updateGuideState(GuideState.TransferStart)
                }
            }
            GuideState.TransferStart -> {
                assert(currentItem != null)
                TransferDrive(currentItem!!, mRobot, tourViewModel)
                Spacer(modifier = Modifier.height(16.dp))
                //TODO aktuell noch Button oder Timer, um die nächste Phase zu triggern (Wechsel zur Exponat-Sequenz)
                CustomButton(
                    title = "Am Exponat angekommen",
                    onClick = { tourViewModel.updateGuideState(GuideState.Exhibit) }
                )
            }

            GuideState.Exhibit -> {
                assert(currentItem != null)
                Exhibit(currentItem!!, mRobot, tourViewModel)

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //TODO aktuell noch Button oder Mechanismus, um zur nächsten Übergangssequenz zu wechseln
                    CustomButton(
                        title = "Zum nächsten Exponat",
                        onClick = {
                            tourViewModel.updateGuideState(GuideState.TransferStart)
                            tourViewModel.incrementCurrentItemIndex()
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

            }

            GuideState.TransferGoing -> TODO()
            GuideState.End -> TODO()
        }
    }

}
