package com.jeremy.acw.ui.components.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.core.utils.dateFormatter2
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.SecondarySecondary
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingDateItem(
    date: LocalDate,
    selected: Boolean = false,
    onClicked: () -> Unit
) {
    val convertedDate = date.format(dateFormatter2()).split(" ")
    val day = convertedDate[0]
    val dayNum = convertedDate[1]
    val month = convertedDate[2]

    Card(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(2.2f/ 3f)
            .clickable { onClicked() },
        colors = CardDefaults.cardColors(
            containerColor = if(selected) Primary else SecondarySecondary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    day,
                    fontWeight = FontWeight.ExtraLight,
                    color = KindaWhite
                )
                Text(
                    dayNum,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if(selected) Color.Black else KindaWhite
                )
                Text(
                    month,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraLight,
                    color = if(selected) KindaWhite else Primary
                )
            }
        }
    }
}