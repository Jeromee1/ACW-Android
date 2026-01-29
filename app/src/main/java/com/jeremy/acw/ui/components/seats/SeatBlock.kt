package com.jeremy.acw.ui.components.seats

sealed class SeatBlock {
    data class Seats(val count: Int) : SeatBlock()
    object Gap : SeatBlock()
}