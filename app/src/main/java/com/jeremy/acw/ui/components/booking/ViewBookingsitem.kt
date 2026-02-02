package com.jeremy.acw.ui.components.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.core.utils.dateFormatter
import com.jeremy.acw.core.utils.timeFormatter
import com.jeremy.acw.core.utils.timestampToDate
import com.jeremy.acw.core.utils.timestampToTime
import com.jeremy.acw.data.model.cinema.Booking
import com.jeremy.acw.ui.theme.BlackT
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary
import com.jeremy.acw.ui.theme.SecondarySecondary

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewBookingsItem(
    booking: Booking,
    onLongPressed: () -> Unit
) {
    val displayDate = timestampToDate(booking.startTime!!).format(dateFormatter())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = { onLongPressed() }
            ),
        colors = CardDefaults.cardColors(
            containerColor = SecondarySecondary
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Secondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp)
        ) {
            Text(
                booking.movieTitle,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlackT)
                    .padding(8.dp),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 12.dp, 20.dp, 0.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    booking.theatreId,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Text(
                    "Hall: ${booking.hallName}",
                    fontSize = 18.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Seats:")
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(booking.seats) {
                            Text(it,)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        displayDate,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        "${
                            timestampToTime(booking.startTime).format(timeFormatter())
                        } - ${
                            timestampToTime(booking.endTime!!).format(timeFormatter())
                        }"
                    )
                }
            }
        }
    }
}