package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.fhkiel.temi.robogguide.models.Item
import de.fhkiel.temi.robogguide.models.LevelOfDetail
import de.fhkiel.temi.robogguide.models.Location
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel

/**
 * LocationPreview
 * - shows an image, the name and the possibility to look at the items of a location or to navigate to it
 * @param location: the location to be displayed
 * @param navHostController: the navigation controller
 * @param showExhibitions: the state to show the exhibitions
 * @param tourViewModel: the view model to handle the tour
 */
@Composable
fun LocationPreview(
    location: Location,
    navHostController: NavHostController,
    showExhibitions: MutableState<String>,
    tourViewModel: TourViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // show an image if it exists, otherwise show a placeholder image
            if (location.conciseText?.mediaList?.firstOrNull()?.url != null) {
                LoadingImage(
                    urlString = location.conciseText.mediaList.first().url.toString(),
                    modifier = Modifier.size(400.dp)
                )
            } else {
                StockImage()
            }
            Spacer(modifier = Modifier.width(32.dp))
            Column {
                Text(
                    text = location.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = location.conciseText?.value ?: "Keine Beschreibung vorhanden.",
                    fontSize = 24.sp,
                    modifier = Modifier.width(400.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.width(32.dp))
        Column {
            // show a button to navigate to the exhibits if a location has exhibits
            if (location.items.isNotEmpty()) {
                CustomButton(
                    onClick = {
                        showExhibitions.value = location.name
                    },
                    title = "Ausstellungsstücke anschauen",
                    fontSize = 24.sp,
                    width = 400.dp,
                    height = 100.dp,
                    initialBackgroundColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, end = 8.dp),
                )
            } else {
                Text(
                    text = "Keine Ausstellungsstücke vorhanden",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 8.dp,
                        bottom = 24.dp,
                        end = 8.dp
                    ),
                )
            }
            CustomButton(
                onClick = {
                    // prepare the detailed exhibit page
                    tourViewModel.fillTourLocations(listOf(location).toMutableList())
                    tourViewModel.levelOfDetail = LevelOfDetail.EVERYTHING_DETAILED

                    // navigate to the detailed exhibit page
                    navHostController.navigate("detailedExhibit")
                },
                title = "Führe mich dorthin!",
                fontSize = 24.sp,
                width = 400.dp,
                height = 100.dp,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp, end = 8.dp),
            )
        }
    }
}

/**
 * ItemPreview
 * - shows an image, the name and the possibility to navigate to the exhibit via the robot
 * @param item: the item to be displayed
 * @param tourViewModel: the view model to handle the tour
 * @param navHostController: the navigation controller
 */
@Composable
fun ItemPreview(
    item: Item,
    tourViewModel: TourViewModel,
    navHostController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // show an image if it exists, otherwise show a placeholder image
            if (item.conciseText?.mediaList?.firstOrNull()?.url != null) {
                LoadingImage(
                    urlString = item.conciseText.mediaList.first().url.toString(),
                    modifier = Modifier.size(400.dp)
                )
            } else {
                StockImage()
            }
            Spacer(modifier = Modifier.width(32.dp))
            Column {
                Text(
                    text = item.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = item.conciseText?.value ?: "Keine Beschreibung vorhanden.",
                    fontSize = 24.sp,
                    modifier = Modifier.width(400.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.width(32.dp))
        CustomButton(
            onClick = {
                // prepare the detailed exhibit page
                tourViewModel.fillTourLocations(listOf(item.location!!).toMutableList())
                tourViewModel.levelOfDetail = LevelOfDetail.EVERYTHING_DETAILED
                // set the current item to the selected item
                item.location!!.items.forEachIndexed { index, it ->
                    if (it.name == item.name) {
                        tourViewModel.currentItemIndex.value = index + 1
                        return@forEachIndexed
                    }
                }
                tourViewModel.currentItem.value = item
                // navigate to the detailed exhibit page
                navHostController.navigate("detailedExhibit")
            },
            title = "Führe mich dorthin!",
            fontSize = 24.sp,
            width = 400.dp,
            height = 100.dp,
            modifier = Modifier.padding(16.dp),
        )
    }
}
