package com.jeremy.acw.data.model.cinema

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.jeremy.acw.data.enums.cinema.BookingStatus

data class Booking(
    val id: String = "",
    val uid: String = "",
    val screeningId: String = "",
    val movieTitle: String = "",
    val hallName: String = "",
    val dateTime: Timestamp,
    val seats: List<String> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: String = BookingStatus.ONGOING.value,
    val bookedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> = mutableMapOf(
        "id" to id,
        "uid" to uid,
        "screeningId" to screeningId,
        "movieTitle" to movieTitle,
        "hallName" to hallName,
        "dateTime" to dateTime,
        "seats" to seats,
        "totalPrice" to totalPrice,
        "status" to status,
        "bookedAt" to FieldValue.serverTimestamp()
    )
}