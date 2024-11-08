package de.fhkiel.temi.robogguide.ui.theme.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun EndScreen(innerPadding: PaddingValues, navHostController: NavHostController, robot: Robot?) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header(
            title = "Wie hat Ihnen die Führung gefallen?",
            fontSize = 64.sp,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.reshot_icon_normal_ny3m982b6q),
                contentDescription = "Lächelndes Gesicht",
                modifier = Modifier
                    .size(250.dp)
                    .clickable {
                        /* positive feedback */
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
                        /* neutral feedback */
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
                        /* negative feedback */
                    },
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
        CustomButton(
            title = "Führung beenden",
            onClick = {
                navHostController.navigate("homePage")
            },
        )
    }
}