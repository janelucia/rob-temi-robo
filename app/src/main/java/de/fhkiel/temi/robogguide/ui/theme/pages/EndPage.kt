package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.robotemi.sdk.Robot
import de.fhkiel.temi.robogguide.R
import de.fhkiel.temi.robogguide.ui.theme.components.CustomButton
import de.fhkiel.temi.robogguide.ui.theme.components.Header

@Composable
fun EndPage(innerPadding: PaddingValues, navHostController: NavHostController, robot: Robot?) {
    var feedbackGiven by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!feedbackGiven) {
                Header(
                    title = "Wie hat Ihnen die Führung gefallen?",
                    fontSize = 64.sp,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.reshot_icon_normal_ny3m982b6q),
                        contentDescription = "Lächelndes Gesicht",
                        modifier = Modifier
                            .size(250.dp)
                            .clickable {
                                feedbackGiven = true
                            },
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.reshot_icon_big_frown_za7k8vxwd6),
                        contentDescription = "Neutrales Gesicht",
                        modifier = Modifier
                            .size(250.dp)
                            .clickable {
                                feedbackGiven = true
                            },
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.reshot_icon_upset_yqdzur4hxj),
                        contentDescription = "Trauriges Gesicht",
                        modifier = Modifier
                            .size(250.dp)
                            .clickable {
                                feedbackGiven = true
                            },
                        contentScale = ContentScale.Fit
                    )
                }
            } else {
                Header(
                    title = "Führung beenden?",
                    fontSize = 64.sp,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CustomButton(
                        title = "Den Roboter zur Ladestation schicken",
                        onClick = {
                            navHostController.navigate("homePage")
                        },
                        initialBackgroundColor = Color.White,
                        contentColor = Color.Black,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 32.sp,
                        height = 100.dp
                    )
                    CustomButton(
                        title = "Zum Startbildschirm zurückkehren",
                        onClick = {
                            navHostController.navigate("homePage")
                        },
                        modifier = Modifier.padding(16.dp),
                        fontSize = 32.sp,
                        height = 100.dp
                    )
                }
            }
        }
}