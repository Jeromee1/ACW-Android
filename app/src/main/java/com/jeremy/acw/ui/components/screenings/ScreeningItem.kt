package com.jeremy.acw.ui.components.screenings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.core.utils.toTimeString
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun ScreeningItem(
    screening: Screening
) {
    val displayStartTime = screening.startTime!!.toDate().toTimeString()
    val displayEndTime = screening.endTime!!.toDate().toTimeString()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Secondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                screening.movieTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                )
            Text(
                "$displayStartTime - $displayEndTime",
                fontSize = 20.sp
            )
        }
    }
}
