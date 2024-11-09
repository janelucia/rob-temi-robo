package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.Text
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
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (tourViewModel.levelOfDetail?.isDetailed() == true) {
            DisplayMedia(currentItem.detailedText!!)
            DisplayMedia(currentItem.conciseText!!)
        } else {
            DisplayMedia(currentItem.conciseText!!)
        }
    }

}
