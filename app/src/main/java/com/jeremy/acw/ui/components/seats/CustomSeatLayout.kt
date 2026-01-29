package com.jeremy.acw.ui.components.seats

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.data.enums.cinema.SeatLayout
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun CustomSeatLayout(
    size: String,
    seatMap: Map<String, SeatStatus>,
    onSeatSelected: (String) -> Unit
) {
    val smallArr = listOf(
        SeatBlock.Seats(6)
    )
    val mediumArr = listOf(
        SeatBlock.Seats(2),
        SeatBlock.Gap,
        SeatBlock.Seats(6),
        SeatBlock.Gap,
        SeatBlock.Seats(2)
    )
    val rows = 6

    val arrangement = when (size) {
        SeatLayout.SMALL.value -> smallArr
        SeatLayout.MEDIUM.value -> mediumArr
        else -> smallArr
    }

    fun seatId(rowIndex: Int, seatNumber: Int): String {
        val rowChar = ('A' + rowIndex)
        return "$rowChar-$seatNumber"
    }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(0))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.7f, 3f)
                    offset += pan
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .wrapContentWidth(unbounded = true, align = Alignment.CenterHorizontally)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(18.dp)
                    .background(Secondary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Screen", fontSize = 10.sp, color = KindaWhite)
            }

            Spacer(Modifier.height(80.dp))

            repeat(rows) { rowIndex ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var seatCounter = 1
                    arrangement.forEach { block ->
                        when (block) {
                            SeatBlock.Gap -> {
                                Spacer(Modifier.width(16.dp))
                            }
                            is SeatBlock.Seats -> {
                                repeat(block.count) {
                                    val id = seatId(rowIndex, seatCounter)
                                    val status = seatMap[id] ?: SeatStatus.VACANT

                                    SeatItem(
                                        seatId = id,
                                        status = status,
                                        onClick = { onSeatSelected(it) }
                                    )
                                    seatCounter++
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}