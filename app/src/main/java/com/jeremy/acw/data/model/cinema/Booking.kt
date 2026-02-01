package com.jeremy.acw.data.model.cinema

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.jeremy.acw.data.enums.cinema.BookingStatus
import kotlin.requireNotNull

data class Booking(
    val id: String = "",
    val uid: String = "",
    val theatreId: String = "",
    val screeningId: String = "",
    val movieTitle: String = "",
    val hallId: String = "",
    val hallName: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val seats: List<String> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: String = BookingStatus.ONGOING.value,
    val bookedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        requireNotNull(startTime) { "Starting time must be set" }
        requireNotNull(endTime) { "Ending time must be set" }

        return mutableMapOf(
            "id" to id,
            "uid" to uid,
            "theatreId" to theatreId,
            "screeningId" to screeningId,
            "movieTitle" to movieTitle,
            "hallId" to hallId,
            "hallName" to hallName,
            "startTime" to startTime,
            "endTime" to endTime,
            "seats" to seats,
            "totalPrice" to totalPrice,
            "status" to status,
            "bookedAt" to FieldValue.serverTimestamp()
        )
    }
}