package com.jeremy.acw.data.model.forms

import com.jeremy.acw.data.enums.cinema.AgeRating
import com.jeremy.acw.data.enums.cinema.MovieStatus

data class UpdateMovieForm(
    val title: String = "",
    val desc: String = "",
    val duration: Long = 0L,
    val genre: List<String> = emptyList(),
    val language: List<String> = emptyList(),
    val subtitles: List<String> = emptyList(),
    val ageRating: String = AgeRating.G.value,
    val status: String = MovieStatus.COMING_SOON.value,
    val imgUrl: String = ""
)