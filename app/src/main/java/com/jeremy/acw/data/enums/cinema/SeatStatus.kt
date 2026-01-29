package com.jeremy.acw.data.enums.cinema

enum class SeatStatus {
    VACANT,
    BOOKED,
    MAINTENANCE,
    UNAVAILABLE;

    companion object {
        fun fromString(value: String) =
            runCatching { valueOf(value) }.getOrDefault(VACANT)
    }
}