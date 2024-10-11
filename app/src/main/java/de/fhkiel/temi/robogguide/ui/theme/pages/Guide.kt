package com.example.rob_temi_robo_ui.ui.theme.pages

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rob_temi_robo_ui.R
import com.example.rob_temi_robo_ui.ui.theme.components.Header

@Composable
fun Guide(innerPadding: PaddingValues, navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Header(
            title = "Kurze Führung (30 Minuten)",
            fontSize = 32.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Header(
            title = "Bitte folgen Sie mir!",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Header(
            title = "Das nächste Exponat ist:",
            fontSize = 32.sp
        )
        Image(
            painter = painterResource(id = R.drawable.computermuseum_maerz_23),
            contentDescription = "Example Image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}
