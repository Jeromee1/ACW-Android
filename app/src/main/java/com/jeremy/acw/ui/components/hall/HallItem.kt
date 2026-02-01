package com.jeremy.acw.ui.components.hall

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun HallItem(
    hall: Hall,
    onLongPressed: () -> Unit,
    onClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .background(Secondary, RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = { onClicked() },
                onLongClick = { onLongPressed() }
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            hall.seatLayout,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Text(
            hall.hallName,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            hall.hallType,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}