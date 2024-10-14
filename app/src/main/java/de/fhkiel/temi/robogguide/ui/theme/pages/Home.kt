package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import de.fhkiel.temi.robogguide.ui.theme.components.Header
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton

@Composable
fun Home(innerPadding: PaddingValues, navHostController: NavHostController, mRobot: Robot?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header(
            title = "Willkommen im Computermuseum!",
            modifier = Modifier.padding(16.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Header(
            title = "Du befindest dich in ${mRobot?.locations?.get(0).toString()}!",
            fontSize = 64.sp,
            fontWeight = FontWeight.Normal
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)) {
            CustomButton(
                title = "Los gehts!",
                onClick = {navHostController.navigate("guideSelector")}
            )
            Spacer(modifier = Modifier.width(32.dp))
            CustomButton(
                title = "Tutorial",
                backgroundColor = Color.White,
                contentColor = Color.Black,
                onClick = {}
            )
        }
    }
}
