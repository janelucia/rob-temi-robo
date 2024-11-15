package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.ItemPreview
import de.fhkiel.temi.robogguide.ui.theme.components.LocationPreview

/**
 * GuideExhibition: user can choose which location or item he is particularly interested in and being guided to it.
 * @param innerPadding: PaddingValues - the padding values.
 * @param tourManager: TourManager - the tour manager.
 * @param tourViewModel: TourViewModel - the view model for the tour.
 * @param navHostController: NavHostController - the navigation controller.
 */
@Composable
fun GuideExhibition(
    innerPadding: PaddingValues,
    tourManager: TourManager,
    tourViewModel: TourViewModel,
    navHostController: NavHostController
) {
    // Create LazyListState to own it and be able to control scroll behaviour of scrollable Row below
    val listState = rememberLazyListState()
    val showExhibitions = remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        state = listState,
    ) {
        item {
            Text(
                text = "Wovon darf ich dir erzählen?",
                modifier = Modifier.padding(16.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 64.sp)
            )
        }
        if (showExhibitions.value.isNotEmpty()) {
            item {
                Text(buildAnnotatedString {
                    append("Du schaust dir die Ausstellungsstücke von Raum: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(showExhibitions.value)
                    }
                    append(" an.")
                },
                    style = TextStyle(fontSize = 32.sp),
                    modifier = Modifier.padding(16.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Wähle das Ausstellungsstück, welches dich interessiert!",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(fontSize = 32.sp),
                    )
                    CustomButton(
                        onClick = {
                            showExhibitions.value = ""
                        },
                        title = "Zurück zu den Stationen",
                        fontSize = 24.sp,
                        width = 400.dp,
                        height = 100.dp,
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
            tourManager.selectedPlace?.allLocations?.find { location: Location ->
                location.name == showExhibitions.value
            }?.items?.forEach { item: Item ->
                item {
                    ItemPreview(item, tourViewModel, navHostController)
                }
            }
        } else {
            item {
                Text(
                    text = "Wähle die Station, die dich interessiert!",
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(fontSize = 32.sp),
                )
            }
            tourManager.selectedPlace?.allLocations?.forEach { location: Location ->
                item {
                    LocationPreview(location, navHostController, showExhibitions, tourViewModel)
                }
            }
        }
    }
}