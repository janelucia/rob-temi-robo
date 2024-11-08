package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.models.LevelOfDetail
import de.fhkiel.temi.robogguide.ui.logic.TourViewModel
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.Header

@Composable
fun GuideSelector(
    innerPadding: PaddingValues,
    navHostController: NavHostController,
    tourManager: TourManager,
    tourViewModel: TourViewModel
) {
    var isGuideSelected by remember { mutableStateOf(false) }
    var selectedLength by remember { mutableStateOf("") }
    //TODO make use of Tour object ???

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isGuideSelected) {
            if (selectedLength.isNotEmpty()) {
                Header(
                    title = buildAnnotatedString {
                        append("Du hast die ")
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(convertTourName(selectedLength))
                        }
                        append(" Tour gewählt.")
                    }.toString(),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(32.dp))
                Header(
                    title = "Wähle, wie ausführlich die Informationen sein sollen.",
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        title = "Nur die wichtigsten Informationen",
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black,
                        width = 800.dp,
                        onClick = {

                            if (selectedLength == "Only Important") {
                                tourViewModel.levelOfDetail = LevelOfDetail.ONLY_IMPORTANT_CONCISE
                            } else if (selectedLength == "Everything") {
                                tourViewModel.levelOfDetail = LevelOfDetail.EVERYTHING_CONCISE
                            }
                            navHostController.navigate("guide")
                        }
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    CustomButton(
                        title = "Alle Informationen",
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black,
                        width = 800.dp,
                        onClick = {
                            if (selectedLength == "Only Important") {
                                tourViewModel.levelOfDetail = LevelOfDetail.ONLY_IMPORTANT_DETAILED
                            } else if (selectedLength == "Everything") {
                                tourViewModel.levelOfDetail = LevelOfDetail.EVERYTHING_DETAILED
                            }

                            navHostController.navigate("guide")
                        }
                    )
                }
            } else {
                Header(
                    title = "Wähle die Länge deiner Führung",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomButton(
                        title = "Highlights Führung\n(${tourManager.selectedPlace?.importantLocations?.size} Stationen)",
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black,
                        width = 800.dp,
                        modifier = Modifier.wrapContentSize(),
                        onClick = {
                            selectedLength = "Only Important"
                            tourManager.selectedPlace?.importantLocations?.let {
                                tourViewModel.fillTourLocations(
                                    it.toMutableList()
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    CustomButton(
                        title = "Ausführliche Führung\n" +
                                "(${tourManager.selectedPlace?.allLocations?.size} Stationen)",
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black,
                        width = 800.dp,
                        onClick = {
                            selectedLength = "Everything"
                            tourManager.selectedPlace?.allLocations?.let {
                                tourViewModel.fillTourLocations(
                                    it.toMutableList()
                                )
                            }
                        }
                    )
                }
            }
        } else {
            Header(
                title = "Was möchtest du machen?",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(32.dp))
            Header(
                title = "Wähle, ob du eine Führung oder einzelne Ausstellungsstücke sehen möchtest.",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 32.dp, horizontal = 16.dp)) {
                CustomButton(
                    title = "Führung",
                    onClick = { isGuideSelected = true }
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "Stationen & Ausstellungsstücke",
                    initialBackgroundColor = Color.White,
                    contentColor = Color.Black,
                    onClick = { navHostController.navigate("guideExhibition") },
                    width = 800.dp
                )
            }
        }
    }
}

fun convertTourName(tourName: String): String {
    return when (tourName) {
        "Only Important" -> "Highlights"
        "Everything" -> "Ausführliche"
        else -> "Fehler"
    }
}
