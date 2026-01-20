package com.jeremy.acw.data.model.cinema

import com.jeremy.acw.data.enums.cinema.AgeRating
import com.jeremy.acw.data.enums.cinema.MovieStatus

data class Movie(
    val id: String = "",
    val title: String = "",
    val desc: String = "",
    val duration: Long = 0L,
    val genre: List<String> = emptyList(),
    val language: List<String> = emptyList(),
    val subtitles: List<String> = emptyList(),
    val ageRating: String = AgeRating.G.value,
    val status: String = MovieStatus.COMING_SOON.value,
    val imgUrl: String = ""
) {
    fun toMap(): Map<String, Any> = mutableMapOf(
        "id" to id,
        "title" to title,
        "desc" to desc,
        "duration" to duration,
        "genre" to genre,
        "language" to language,
        "subtitles" to subtitles,
        "ageRating" to ageRating,
        "status" to status,
        "imgUrl" to imgUrl,
    )
}