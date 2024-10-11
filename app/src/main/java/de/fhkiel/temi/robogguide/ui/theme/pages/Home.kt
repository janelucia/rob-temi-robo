package com.example.rob_temi_robo_ui.ui.theme.pages

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rob_temi_robo_ui.ui.theme.components.Header
import com.example.rob_temi_robo_ui.ui.theme.components.CustomButton

@Composable
fun Home(innerPadding: PaddingValues, navHostController: NavHostController) {
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
            title = "Du befindest dich in Location!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)) {
            CustomButton(
                title = "Los gehts!",
                width = 400.dp,
                onClick = {navHostController.navigate("guideSelector")}
            )
            Spacer(modifier = Modifier.width(16.dp))
            CustomButton(
                title = "Tutorial",
                width = 200.dp,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                onClick = {}
            )
        }
    }
}
