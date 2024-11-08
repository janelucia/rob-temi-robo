package de.fhkiel.temi.robogguide.ui.theme.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun GuideProgressBar(numberOfExhibits: Int, currentExhibit: Int) {


    // Fortschrittsanzeige
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
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
            repeat(numberOfExhibits) {
                Log.d("Test", "current bead ${it}")
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, Color.Black, shape = ButtonDefaults.outlinedShape)
                        .background(
                            if (it == currentExhibit) Color.Black else Color.White,
                            CircleShape
                        )
                        .padding(4.dp)
                )
                if (it < numberOfExhibits - 1) {
                    // add a small line between each 'bead'
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .height(2.dp)
                            .background(Color.Gray)
                            .align(Alignment.CenterVertically)
                    )

                }
            }
        }
    }
}