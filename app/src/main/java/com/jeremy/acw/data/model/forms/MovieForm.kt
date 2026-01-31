package com.jeremy.acw.data.model.forms

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.jeremy.acw.core.utils.makeTimestamp
import com.jeremy.acw.data.enums.cinema.AgeRating
import com.jeremy.acw.data.enums.cinema.MovieStatus
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class MovieForm(
    val imgUrl: String = "",
    val title: String = "",
    val description: String = "",
    val duration: Long = 0L,
    val genre: List<String> = emptyList(),
    val language: List<String> = emptyList(),
    val subtitles: List<String> = emptyList(),
    val ageRating: String = AgeRating.G.value,
    val status: String = MovieStatus.COMING_SOON.value,
    val releaseDate: Timestamp = makeTimestamp(LocalDate.now())
)