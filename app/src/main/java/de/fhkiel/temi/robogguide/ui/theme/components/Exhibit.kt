package de.fhkiel.temi.robogguide.ui.theme.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

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
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    ) {
        if(tourViewModel.levelOfDetail?.isDetailed() == true){
            DisplayMedia(context, currentItem.detailedText!!)
            DisplayMedia(context, currentItem.conciseText!!)
        } else {
            DisplayMedia(context, currentItem.conciseText!!)
        }
    }

}

@Composable
fun DisplayMedia(context: Context, text: Text) {
    ShowVideo(context, url = text.media?.url.toString())
   /*if (text.media?.url.toString().isEmpty()) {
       StockImage()
   } else if (text.media?.url?.host == "www.youtube.com") {
       Log.i("Exhibit", "Video kann ich noch nicht")
       ShowVideo(url = text.media.url.toString())
   } else {
       Log.i("Exhibit", "Bild kann ich noch nicht")
       LoadingImage(
           urlString = text.media?.url.toString(),
           modifier = Modifier.size(400.dp),
           contentScale = ContentScale.Fit
       )
       Spacer(modifier = Modifier.width(16.dp))
   }*/
}


