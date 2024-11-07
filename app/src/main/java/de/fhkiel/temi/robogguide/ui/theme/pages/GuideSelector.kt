package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.Header

@Composable
fun GuideSelector(
    innerPadding: PaddingValues,
    navHostController: NavHostController,
    mRobot: Robot?,
    tourManager: TourManager,
    tourViewModel: TourViewModel
) {
    var isGuideSelected by remember { mutableStateOf(false) }
    var isExhibitSelected by remember { mutableStateOf(false) }
    var selectedLength by remember { mutableStateOf("") }
    var selectedInfoLoad by remember { mutableStateOf("") }
    //TODO make use of Tour object ???

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header(
            title = "Wähle deine Tour!",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(32.dp))
        if (isGuideSelected) {
            if (selectedLength.isNotEmpty()) {
                Text(
                    text = buildAnnotatedString {
                        append("Du hast die ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(convertTourName(selectedLength))
                        }
                        append(" Tour gewählt.")
                    },
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        title = "Alle Informationen zu jeder Station",
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        width = 800.dp,
                        onClick = {
                            selectedInfoLoad = "All"
                            tourManager.selectedPlace?.allLocations?.let {
                                tourViewModel.fillTourLocations(
                                    it.toMutableList()
                                )
                            }
                            navHostController.navigate("guide")
                        }
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    CustomButton(
                        title = "Nur die Highlights jeder Station",
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        width = 800.dp,
                        onClick = {
                            selectedInfoLoad = "Highlights"
                            tourManager.selectedPlace?.importantLocations?.let {
                                tourViewModel.fillTourLocations(
                                    it.toMutableList()
                                )
                            }
                            navHostController.navigate("guide")
                        }
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        title = "Kurze Führung\n(? Stationen)",
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        width = 800.dp,
                        modifier = Modifier.wrapContentSize(),
                        onClick = { selectedLength = "Short" }
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    CustomButton(
                        title = "Lange Führung\n(Alle Stationen)",
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                        width = 800.dp,
                        onClick = { selectedLength = "Long" }
                    )
                }
            }
        } else if (isExhibitSelected) {
            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)) {
                CustomButton(
                    title = "Exponat 1",
                    onClick = { /* Handle exhibit 1 selection */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "Exponat 2",
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    onClick = { /* Handle exhibit 2 selection */ }
                )
            }
            /* this needs to be a for loop where all exhibits are displayed*/
        } else {
            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)) {
                CustomButton(
                    title = "Führung",
                    onClick = { isGuideSelected = true }
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "Exponat",
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    onClick = { isExhibitSelected = true }
                )
            }
        }
    }
}

fun convertTourName(tourName: String): String {
    return when (tourName) {
        "Short" -> "Kurze Führung"
        "Long" -> "Lange Führung"
        else -> "Fehler"
    }
}
