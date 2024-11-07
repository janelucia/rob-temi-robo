package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.Location
import androidx.compose.material3.Text as Text

@Composable
fun LocationPreview(location: Location, mRobot: Robot?, showExhibitions: MutableState<String>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = location.name)
            Spacer(modifier = Modifier.width(32.dp))
            if (location.conciseText != null) {
                TextDisplay(text = location.conciseText)
            }
        }
        Spacer(modifier = Modifier.width(32.dp))
        Row {
            CustomButton(
                onClick = {
                    showExhibitions.value = location.name
                },
                title="Exponate anschauen",
                fontSize = 16.sp,
                width = 200.dp,
                height = 50.dp,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.padding(16.dp),
            )
            CustomButton(
                onClick = {
                    mRobot?.goTo(location = location.name)
                    Log.i("LocationPreview", "Navigating to ${location.name}")
                    Log.d("LocationPreview", "${mRobot?.isReady}")
                },
                title = "Zeig mir den Weg!",
                fontSize = 16.sp,
                width = 200.dp,
                height = 50.dp,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
fun ItemPreview(item: Item, mRobot: Robot?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.name)
            Spacer(modifier = Modifier.width(32.dp))
            if (item.conciseText != null) {
                TextDisplay(text = item.conciseText)
            }
        }
        Spacer(modifier = Modifier.width(32.dp))
        Row {
            CustomButton(
                onClick = {
                    mRobot?.goTo(location = item.name)
                    Log.i("ItemPreview", "Navigating to ${item.name}")
                    Log.d("ItemPreview", "${mRobot?.isReady}")
                },
                title = "Zeig mir den Weg!",
                fontSize = 16.sp,
                width = 200.dp,
                height = 50.dp,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
