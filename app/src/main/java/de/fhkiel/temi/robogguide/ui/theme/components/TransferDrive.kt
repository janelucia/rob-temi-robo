package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    if (guideState == GuideState.TransferStart) {
        LaunchedEffect(currentLocation) {
            delay(3000)
            //Beginnt Fahrt zur nächsten Location
            Log.d(
                "Transfer",
                "Ich fahre los zu ${currentLocation.name}, -> ${tourViewModel.guideState.value}"
            )
            mRobot?.goTo(location = currentLocation.name)
        }
    }


    if (guideState == GuideState.TransferGoing) {

        robotSpeakText(mRobot, "Bitte folgen Sie mir zum Ausstellungsstück!", false)
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

    if (guideState == GuideState.TransferError && showErrorPopup) {
        ErrorPopUp(
            onDismiss = { showErrorPopup = false },
            "Es ist ein Navigationsfehler aufgetreten! :(",
            "Leider habe ich keine Route zum gewünschten Standort gefunden.\nBitte achten Sie darauf, dass ich genug Platz habe, um mich zu bewegen. Treten Sie eventuell einen Schritt zurück oder schieben Sie mich ein wenig von umstehenden Objekten weg und versuchen es gerne nochmal.\nAnsonsten können Sie mich auch zurück zur Ladestation schicken.",
            "",
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
    currentLocation.conciseText?.mediaList?.first().let { media ->
        // Bei Transfers gibt es nur ein Bild, da wir nur wissen müssen, wo es hingeht
        LoadingImage(
            urlString = media?.url.toString(),
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillHeight
        )
    }


}