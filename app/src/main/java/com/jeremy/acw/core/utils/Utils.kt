package com.jeremy.acw.core.utils

import com.google.firebase.Timestamp
import com.jeremy.acw.data.model.forms.UpdateMovieForm
import com.jeremy.acw.data.model.requests.UpdateMovieReq
import java.text.SimpleDateFormat
import java.util.Locale

fun buildUpdateReq(data: UpdateMovieForm): UpdateMovieReq {
    return UpdateMovieReq(
        title = data.title,
        desc = data.desc,
        duration = data.duration,
        genre = data.genre,
        language = data.language,
        subtitles = data.subtitles,
        ageRating = data.ageRating,
        status = data.status,
        imgUrl = data.imgUrl
    )
}

fun timeConverter(timeSeconds: Long): String {
    val totalMinutes = timeSeconds / 60
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

fun timestampFormat(timestamp: Timestamp): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(timestamp.toDate())
}