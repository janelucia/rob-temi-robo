package de.fhkiel.temi.robogguide.ui.theme.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.ItemPreview
import de.fhkiel.temi.robogguide.ui.theme.components.LocationPreview

@Composable
fun GuideExhibition(innerPadding: PaddingValues, mRobot: Robot?, tourManager: TourManager) {
    // Create ScrollState to own it and be able to control scroll behaviour of scrollable Row below
    val scrollState = rememberScrollState()
    val showExhibitions = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Wovon darf ich dir erzählen?",
            modifier = Modifier.padding(16.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp)
        )
        if (showExhibitions.value.isNotEmpty()) {
            Text(buildAnnotatedString {
                append("Du schaust dir die Ausstellungsstücke von Raum: ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(showExhibitions.value)
                }
                append(" an.")
            },
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Wähle das Ausstellungsstück, welches dich interessiert!",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                CustomButton(
                    onClick = {
                        showExhibitions.value = ""
                    },
                    title = "Zurück zu den Stationen",
                    fontSize = 16.sp,
                    width = 200.dp,
                    height = 50.dp,
                    backgroundColor = androidx.compose.ui.graphics.Color.White,
                    contentColor = androidx.compose.ui.graphics.Color.Black,
                    modifier = Modifier.padding(16.dp),
                )
            }
            Log.d("Test", "ShowExhibitions: ${showExhibitions.value}")
            tourManager.selectedPlace?.allLocations?.find { location: Location ->
                location.name == showExhibitions.value
            }?.items?.forEach { item: Item ->
                Log.d("Test", "Item: ${item.name}")
                ItemPreview(item = item, mRobot)
            }
        } else {
            Text(
                text = "Wähle die Station, die dich interessiert!",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineMedium
            )
            tourManager.selectedPlace?.allLocations?.forEach { location: Location ->
                Log.d("Test", "Location: ${location.name}")
                LocationPreview(location = location, mRobot, showExhibitions)
            }
        }
    }
}