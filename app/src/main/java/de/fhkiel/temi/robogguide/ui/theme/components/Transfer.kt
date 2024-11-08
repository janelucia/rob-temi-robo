package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel

@Composable
fun Transfer(currentItem: Item, mRobot: Robot?, tourViewModel: TourViewModel) {

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