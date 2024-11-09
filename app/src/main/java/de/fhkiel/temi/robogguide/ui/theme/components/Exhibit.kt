package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import kotlinx.coroutines.delay

@Composable
fun Exhibit(currentItem: Item, mRobot: Robot?, tourViewModel: TourViewModel) {

    LaunchedEffect(currentItem) {
        tourViewModel.updateAlreadySpoken(false)
        delay(2000)
        assert(tourViewModel.levelOfDetail != null)
        if (tourViewModel.levelOfDetail?.isDetailed() == true) {
            val text = currentItem.conciseText?.value + "\n" + currentItem.detailedText?.value
            robotSpeakText(mRobot, text)
        } else {
            robotSpeakText(mRobot, currentItem.conciseText?.value)
        }
        tourViewModel.updateAlreadySpoken(true)
    }

    Header(
        title = currentItem.name,
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    if (tourViewModel.levelOfDetail?.isDetailed() == true) {
        DisplayMediaList(currentItem.conciseText, currentItem.detailedText)
    } else {
        DisplayMediaList(currentItem.conciseText)
    }
}
