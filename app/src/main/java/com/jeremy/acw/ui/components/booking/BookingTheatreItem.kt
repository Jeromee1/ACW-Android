package com.jeremy.acw.ui.components.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jeremy.acw.core.utils.toTimeString
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun BookingTheatreItem(
    theatre: String,
    screenings: List<Screening>,
    onClicked: (String, String, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(thickness = 1.dp, color = Secondary)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                theatre,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = Primary
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                screenings.forEach { screening ->
                    Card(
                        modifier = Modifier
                            .width(100.dp)
                            .clickable { onClicked(screening.theatreId, screening.hallId, screening.id) },
                        border = BorderStroke(1.dp, Primary),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(screening.startTime!!.toDate().toTimeString())
                            HorizontalDivider(thickness = 1.dp, color = Secondary)
                            Text(
                                screening.hallType,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}