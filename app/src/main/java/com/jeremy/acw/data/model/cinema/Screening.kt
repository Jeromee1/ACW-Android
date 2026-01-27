package com.jeremy.acw.data.model.cinema

import com.google.firebase.Timestamp

data class Screening(
    val id: String = "",
    val movieId: String = "",
    val movieTitle: String = "",
    val theatreId: String = "",
    val hallId: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val price: Double = 0.0
) {
    fun toMap(): Map<String, Any> {
        requireNotNull(startTime) { "Starting time must be set" }
        requireNotNull(endTime) { "Ending time must be set" }

        return mutableMapOf(
            "id" to id,
            "movieId" to movieId,
            "movieTitle" to movieTitle,
            "theatreId" to theatreId,
            "hallId" to hallId,
            "startTime" to startTime,
            "endTime" to endTime,
            "price" to price,
            )
        }
}
