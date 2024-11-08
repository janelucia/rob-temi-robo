package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import kotlinx.coroutines.delay

@Composable
fun TransferDrive(currentItem: Item, mRobot: Robot?, tourViewModel: TourViewModel) {

    //TODO currentLocation anstelle von currentItem(?)
    LaunchedEffect(currentItem) {
        delay(3000)
        //Beginnt Fahrt zur nächsten Location
        mRobot?.goTo(location = currentItem.name)

        //Sprachausgabe TRANSFER TODO: transfer Objekt mit Texten -> in Datenbank einfügen
        if (tourViewModel.levelOfDetail?.isDetailed() == true) {
            val text = currentItem.conciseText?.value + "\n" + currentItem.detailedText?.value
            robotSpeakText(mRobot, text)
        } else {
            robotSpeakText(mRobot, currentItem.conciseText?.value)
        }
    }

    Header(
        title = "Bitte folgen Sie mir!",
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    Header(
        title = "Die nächste Station ist: ${currentItem.name}",
        fontSize = 32.sp
    )
    currentItem.conciseText?.media?.let { media ->
        LoadingImage(
            urlString = media.url.toString(),
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}