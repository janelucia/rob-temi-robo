package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.Location
import androidx.compose.material3.Text as Text
import coil.compose.rememberAsyncImagePainter
import de.fhkiel.temi.robogguide.R

@Composable
fun LocationPreview(location: Location, mRobot: Robot?, showExhibitions: MutableState<String>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (location.conciseText?.media?.url == null) {
                Image(
                    painter = rememberAsyncImagePainter(model = location.conciseText?.media?.url),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(8.dp)
                )
            } else {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.fachhochschule_kiel_logo_03_2022),
                    contentDescription = "Fachhochschule Kiel Logo",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = location.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(32.dp))
        Column {
            CustomButton(
                onClick = {
                    showExhibitions.value = location.name
                },
                title="Ausstellungsstücke anschauen",
                fontSize = 24.sp,
                width = 400.dp,
                height = 100.dp,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, end = 8.dp),
            )
            CustomButton(
                onClick = {
                    mRobot?.goTo(location = location.name)
                    Log.i("LocationPreview", "Navigating to ${location.name}")
                    Log.d("LocationPreview", "${mRobot?.isReady}")
                },
                title = "Zeig mir den Weg!",
                fontSize = 24.sp,
                width = 400.dp,
                height = 100.dp,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp, end = 8.dp),
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
            item.conciseText?.media?.let { media ->
                Image(
                    painter = rememberAsyncImagePainter(model = media.url),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(8.dp)
                )
            }
            Text(
                text = item.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(32.dp))
        }
        Spacer(modifier = Modifier.width(32.dp))
        CustomButton(
            onClick = {
                mRobot?.goTo(location = item.name)
                Log.i("ItemPreview", "Navigating to ${item.name}")
                Log.d("ItemPreview", "${mRobot?.isReady}")
            },
            title = "Zeig mir den Weg!",
            fontSize = 24.sp,
            width = 400.dp,
            height = 100.dp,
            modifier = Modifier.padding(16.dp),
        )
    }
}
