package com.jeremy.acw.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.jeremy.acw.data.model.forms.UpdateMovieForm
import com.jeremy.acw.data.model.requests.UpdateMovieReq
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
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

fun longToStringTimeConverter(timeSeconds: Long): String {
    val totalMinutes = timeSeconds / 60
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun makeTimestamp(date: LocalDate, hour: Int = 10, minute: Int = 0): Timestamp {
    val zone = ZoneId.systemDefault()
    val localDateTime = date.atTime(hour, minute)
    val instant = localDateTime.atZone(zone).toInstant()
    return Timestamp(Date.from(instant))
}

fun timestampToDMY(timestamp: Timestamp): String {
    val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return formatter.format(timestamp.toDate())
}

@RequiresApi(Build.VERSION_CODES.O)
fun timestampToTime(timestamp: Timestamp): LocalTime {
    return timestamp.toDate().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
}

fun Date.toTimeString(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
}

@RequiresApi(Build.VERSION_CODES.O)
fun dateFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("dd MMMM yyyy")
}

@RequiresApi(Build.VERSION_CODES.O)
fun timeFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("HH:mm")
}