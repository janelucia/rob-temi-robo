package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.models.GuideState
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import kotlinx.coroutines.delay

@Composable
fun TransferDrive(currentLocation: Location, mRobot: Robot?, tourViewModel: TourViewModel, tourManager: TourManager) {


    val guideState by tourViewModel.guideState.observeAsState(null)

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

        Log.d("Transfer", "ich versuche zu sprechen :((((, -> ${tourViewModel.guideState.value}")
        val transfers = tourManager.selectedPlace?.allTransfers?.get(currentLocation.name)
        if (tourViewModel.levelOfDetail?.isDetailed() == true) {
            val text = transfers?.detailedText?.value
            robotSpeakText(mRobot, text)
        } else {
            val text = transfers?.conciseText?.value
            robotSpeakText(mRobot, text)
        }
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
    currentLocation.conciseText?.media?.let { media ->
        // Bei Transfers gibt es nur ein Bild, da wir nur wissen müssen, wo es hingeht
        LoadingImage(
            urlString = media.url.toString(),
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}