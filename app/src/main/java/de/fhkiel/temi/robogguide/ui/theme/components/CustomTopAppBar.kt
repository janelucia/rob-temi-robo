package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import de.fhkiel.temi.robogguide.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    Box(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.fachhochschule_kiel_logo_03_2022),
                        contentDescription = "Fachhochschule Kiel Logo",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            },
            actions = {
                if (currentDestination != "homePage") {
                    IconButton(
                        onClick = { navController.navigate("homePage") },
                        modifier = Modifier
                            .size(50.dp)
                            .border(2.dp, Color.Black, CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "EN",
                    onClick = {},
                    width = 100.dp,
                    height = 50.dp,
                    fontSize = 32.sp,
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    borderColor = Color.Black,
                    borderWidth = 2.dp,
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomButton(
                    title = "?",
                    onClick = {},
                    width = 60.dp,
                    height = 55.dp,
                    fontSize = 32.sp,
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    borderColor = Color.Black,
                    borderWidth = 2.dp,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (currentDestination == "guide") {
            // Exponat Titel anzeigen
            Header(
                title = "AKTUELLES EXPONAT",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.Center) // Header mittig auf dem GESAMTEN BILDSCHIRM platzieren
                    .padding(top = 8.dp)

            )
        }

    }
}
