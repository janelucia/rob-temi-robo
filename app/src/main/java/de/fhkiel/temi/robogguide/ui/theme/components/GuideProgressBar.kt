package de.fhkiel.temi.robogguide.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun GuideProgressBar() {


    // Titel des Menüs, bspw. aktuelles Exponat
    Header(
        title = "AKTUELLES EXPONAT",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )

    // Fortschrittsanzeige
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        //Header(title = "Fortschritt: 30%", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))

        // Fortschrittsbalken mit Elementen??
        Row(
            modifier = Modifier
                .padding(4.dp)
                .clickable { /*Dinge*/ }
        ) {
            //Fortschrittselemente, eines hervorgehoben (=das aktuelle)
            repeat(5) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, Color.Black, shape = ButtonDefaults.outlinedShape)
                        .background(
                            if (it == 2) Color.Black else Color.White,
                            CircleShape
                        )
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}