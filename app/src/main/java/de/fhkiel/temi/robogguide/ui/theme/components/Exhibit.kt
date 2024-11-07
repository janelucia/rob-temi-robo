package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.R
import de.fhkiel.temi.robogguide.logic.robotSpeakText
import de.fhkiel.temi.robogguide.models.Item
import kotlinx.coroutines.delay

@Composable
fun Exhibit(currentItem: Item, mRobot: Robot?) {

    LaunchedEffect(currentItem) {
        delay(2000)
        robotSpeakText(mRobot, currentItem.conciseText?.value)
    }


    Header(
        title = currentItem.name,
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(16.dp))
    Header(
        title = currentItem.conciseText?.value,
        fontSize = 16.sp
    )
    Header(
        title = currentItem.detailedText?.value,
        fontSize = 12.sp,
        modifier = Modifier.padding(16.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    // Optional: Bild oder weitere Details
    Image(
        painter = painterResource(id = R.drawable.computermuseum_maerz_23),
        contentDescription = "Exponat Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        contentScale = ContentScale.Fit
    )


}