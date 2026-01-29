package com.jeremy.acw.ui.components.seats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Secondary
import com.jeremy.acw.ui.theme.Success

@Composable
fun SeatItem(
    seatId: String,
    status: SeatStatus,
    onClick: (String) -> Unit
) {
    val background = when (status) {
        SeatStatus.BOOKED -> Success
        else -> Secondary
    }

    val icon = when(status) {
        SeatStatus.MAINTENANCE -> Icons.Filled.Handyman
        SeatStatus.UNAVAILABLE -> Icons.Filled.Block
        else -> null
    }

    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(background)
            .clickable(enabled = status != SeatStatus.BOOKED) {
                onClick(seatId)
            },
        contentAlignment = Alignment.Center
    ) {
        if(icon != null)
        Icon(icon, null, tint = KindaWhite)
    }
}