package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.Header

@Composable
fun GuideSelector(innerPadding: PaddingValues, navHostController: NavHostController){
    var isGuideSelected by remember { mutableStateOf(false) }
    var isExhibitSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header(
            title = "Willkommen im Computermuseum!",
        )
        Spacer(modifier = Modifier.height(16.dp))
        Header(
            title = "Was darf ich dir zeigen?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)) {
            if (isGuideSelected) {
                CustomButton(
                    title = "Kurze Führung (30 Minuten)",
                    width = 400.dp,
                    onClick = { navHostController.navigate("guide") }
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "Lange Führung (1 Stunde)",
                    width = 400.dp,
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    onClick = { /* Handle long guide selection */ }
                )
            } else if (isExhibitSelected) {
                CustomButton(
                    title = "Exponat 1",
                    width = 400.dp,
                    onClick = { /* Handle exhibit 1 selection */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "Exponat 2",
                    width = 400.dp,
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    onClick = { /* Handle exhibit 2 selection */ }
                )
                /* this needs to be a for loop where all exhibits are displayed*/
            } else {
                CustomButton(
                    title = "Führung",
                    width = 400.dp,
                    onClick = { isGuideSelected = true }
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "Exponat",
                    width = 400.dp,
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    onClick = { isExhibitSelected = true }
                )
            }
        }
    }
}
