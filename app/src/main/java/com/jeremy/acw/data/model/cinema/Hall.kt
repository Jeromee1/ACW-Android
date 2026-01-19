package com.jeremy.acw.data.model.cinema

import com.jeremy.acw.data.enums.cinema.HallType
import com.jeremy.acw.data.enums.cinema.SeatLayout

data class Hall(
    val id: String = "",
    val theatreId: String = "",
    val hallName: String = "",
    val seatLayout: String = SeatLayout.SMALL.value,
    val hallType: String = HallType.STANDARD.value
) {
    fun toMap(): Map<String, Any> = mutableMapOf(
        "id" to id,
        "theatreId" to theatreId,
        "hallName" to hallName,
        "seatLayout" to seatLayout,
        "hallType" to hallType
    )
}