package de.fhkiel.temi.robogguide.ui.theme.components

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.R
import de.fhkiel.temi.robogguide.logic.TourManager
import de.fhkiel.temi.robogguide.ui.logic.SetupViewModel

@Composable
fun SetupUi(tourManager: TourManager, setupViewModel: SetupViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(tourManager.allPlacesMap.size) } // set to the last element of the list
    val isRobotReady by setupViewModel.isRobotReady.observeAsState(false)
    val context = LocalContext.current as Activity
    val isKioskModeEnabled by setupViewModel.isKioskModeEnabled.observeAsState(false)


    Scaffold(
        topBar = {
            SetupTopBar(context)
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
            )
            Header(
                title = "Bitte wähle aus der Liste von Orten, wo ich eingesetzt werden soll.",
                fontSize = 64.sp,
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
                            text = tourManager.allPlacesMap[selectedIndex]!!,
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
                        .fillMaxSize().padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Header(
                            title = "Bitte wähle aus der Liste von Orten, wo ich eingesetzt werden soll.",
                            fontSize = 32.sp
                        )
                        CustomButton(
                            onClick = {
                                expanded = false
                            },
                            title = "Schließen",
                            modifier = Modifier.padding(16.dp),
                            width = 200.dp,
                            height = 50.dp,
                            fontSize = 24.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    tourManager.allPlacesMap.forEach { (index, placeName) ->
                        DropdownMenuItem(
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.width(50.dp)
                                    ) {
                                        if (index == selectedIndex) {
                                            Image(
                                                painter = painterResource(id = R.drawable.arrow_right),
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(Color.Black),
                                                modifier = Modifier.padding(8.dp).size(50.dp),
                                            )
                                        }
                                    }
                                    Text(
                                        text = placeName,
                                        fontSize = 64.sp,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }

                            },
                            modifier = Modifier.fillMaxWidth().border(
                                width = 2.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(8.dp))

                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Row {
                CustomButton(
                    onClick = {
                        // Set the selected place inside the tour manager
                        tourManager.setPlace(
                            selectedIndex,
                            tourManager.allPlacesMap[selectedIndex]!!
                        )
                        setupViewModel.completeSetup()
                    },
                    title = "Setup beenden",
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                if (isRobotReady) {
                    val mRobot = Robot.getInstance()
                    CustomButton(
                        onClick = {
                            val mapName = mRobot.getMapData()?.mapName
                            var placeFound = false
                            var placeSet = false
                            Log.i("SetupUi", "Reading map name: $mapName")
                            tourManager.allPlacesMap.forEach { (index, placeName) ->
                                if (mapName?.startsWith(placeName) == true) {
                                    placeSet = tourManager.setPlace(index, placeName)
                                    placeFound = true
                                    return@forEach
                                }
                            }
                            if (!placeFound) {
                                Log.e("SetupUi", "Place not found")
                            } else {
                                if (placeSet) {
                                    setupViewModel.completeSetup()
                                } else {
                                    Log.e("SetupUi", "Place not set")
                                }
                            }
                        },
                        title = "Den Roboter auswählen lassen",
                        modifier = Modifier.padding(16.dp)
                    )
                    Column {
                        Text("Enable Kiosk Mode")
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isKioskModeEnabled,
                            onCheckedChange = { enabled: Boolean ->
                                setupViewModel.setKioskModeEnabled(enabled)
                                Log.i("SetupUi", "Kiosk mode enabled: $enabled")
                                if (enabled) {
                                    mRobot.requestToBeKioskApp()
                                }
                            }
                        )
                    }
                } else {
                    LoadingSpinner(
                        messages = listOf("Warte auf Roboter..."),
                        currentMessageIndex = 0
                    )
                }

            }
        }
    }
}