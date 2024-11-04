package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.fhkiel.temi.robogguide.R
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.logic.SetupViewModel

@Composable
fun SetupUi(tourManager: TourManager, setupViewModel: SetupViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(1) } // needs to be one
    Scaffold(
        topBar = {
            Text(
                "Gruppe Pentagram - Temi Setup",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Header(
                title = "Hallo Mensch!",
                modifier = Modifier.padding(16.dp),
                fontSize = 64.sp,
            )
            Header(
                title = "Bitte wähle aus der Liste von Orten, wo ich eingesetzt werden soll.",
                fontSize = 42.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable(onClick = { expanded = true })
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Ort:",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                        )
                        Text(
                            text = tourManager.allPlaces[selectedIndex]!!.name,
                            fontSize = 64.sp,
                        )
                    }
                    Icon(
                        painter = painterResource(
                            id = R.drawable.arrow_drop_down
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .graphicsLayer(
                                rotationZ = if (expanded) 180f else 0f
                            )
                            .size(64.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    scrollState = rememberScrollState(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Log.d("Setup", "All places: ${tourManager.allPlaces}")
                    tourManager.allPlaces.forEach { (index, place) ->
                        DropdownMenuItem(
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = place.name,
                                    fontSize = 64.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        )
                    }
                }
            }

            CustomButton(
                onClick = { setupViewModel.completeSetup() },
                title = "Setup beenden",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}