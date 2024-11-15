package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.models.GuideState
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import kotlinx.coroutines.delay

/**
 * TransferDrive: displays the current location and the next location to drive to
 */
@Composable
fun TransferDrive(
    currentLocation: Location,
    mRobot: Robot?,
    tourViewModel: TourViewModel,
    tourManager: TourManager,
    navController: NavController
) {
    val guideState by tourViewModel.guideState.observeAsState(null)
    var showErrorPopup by remember { mutableStateOf(true) }

    // when the temi starts to drive to the next location, it says where it is going
    if (guideState == GuideState.TransferStart) {
        LaunchedEffect(currentLocation) {
            delay(3000)
            // Starts drive to next location
            Log.d(
                "Transfer",
                "Ich fahre los zu ${currentLocation.name}, -> ${tourViewModel.guideState.value}"
            )
            mRobot?.goTo(location = currentLocation.name)
        }
    }

    // when the temi is driving to the next location, it tells the visitor to follow
    if (guideState == GuideState.TransferGoing) {
        robotSpeakText(mRobot, "Bitte folgen Sie mir zur nächsten Station", false)
        Log.d("Transfer", "ich spreche und fahre, -> ${tourViewModel.guideState.value}")
        val transfers = tourManager.selectedPlace?.allTransfers?.get(currentLocation.name)
        if (tourViewModel.levelOfDetail?.isDetailed() == true) {
            val text = transfers?.detailedText?.value
            robotSpeakText(mRobot, text)
        } else {
            val text = transfers?.conciseText?.value
            robotSpeakText(mRobot, text)
        }
    }

    // when an error occurs during the transfer, a popup is displayed
    if (guideState == GuideState.TransferError && showErrorPopup) {
        ErrorPopUp(
            onDismiss = { showErrorPopup = false },
            "Es ist ein Navigationsfehler aufgetreten! :(",
            "Leider habe ich keine Route zum gewünschten Standort gefunden.\nBitte achten Sie darauf, dass ich genug Platz habe, um mich zu bewegen. Treten Sie eventuell einen Schritt zurück oder schieben Sie mich ein wenig von umstehenden Objekten weg und versuchen es gerne nochmal.\nAnsonsten können Sie mich auch zurück zur Ladestation schicken.",
            onClick = {
                tourViewModel.updateGuideState(GuideState.TransferStart)
                showErrorPopup = false
                robotSpeakText(mRobot, "Okay, probieren wir es nochmal!", false)
            },
            navController = navController,
            mRobot = mRobot
        )
    }

    Header(
        title = "Bitte folgen Sie mir!",
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    Header(
        title = "Die nächste Station ist: ${currentLocation.name}",
        fontSize = 32.sp
    )
    // Transfers only has one image in order to tell the user where to go
    currentLocation.conciseText?.mediaList?.first().let { media ->
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.width(1300.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                LoadingImage(
                    urlString = media?.url.toString(),
                    modifier = Modifier.align(Alignment.CenterEnd).width(800.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier.width(500.dp).fillMaxHeight(),
            ){
                TemiSpeakingFace()
            }
        }

    }


}