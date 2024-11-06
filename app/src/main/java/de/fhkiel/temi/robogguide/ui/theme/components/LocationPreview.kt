package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.ui.theme.Purple40
import androidx.compose.material3.Text as Text

@Composable
fun LocationPreview(innerPadding: PaddingValues, location: Location, mRobot: Robot?) {

    Row(
        modifier = Modifier
            .background(Purple40)
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        Text(text = location.name)
        Spacer(modifier = Modifier.width(32.dp))
        if (location.conciseText != null) {
            TextDisplay(text = location.conciseText)
        }
        Spacer(modifier = Modifier.width(32.dp))
        Button(
            onClick = {
                mRobot?.goTo(location = location.name)
                Log.i("LocationPreview", "Navigating to ${location.name}")
                Log.d("LocationPreview", "${mRobot?.isReady}")
            },
        ) {
            Text("Bring mich dort hin")
        }
    }

}