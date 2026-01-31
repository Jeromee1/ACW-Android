package com.jeremy.acw.ui.screens.admin.manageMovies.add

import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.forms.MovieForm
import com.jeremy.acw.data.repo.MovieRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AddMovieViewModel @Inject constructor(
    private val movieRepo: MovieRepo
) : BaseViewModel() {
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    fun submit(form: MovieForm) {
        if(!validateMovie(form)) return
        viewModelScope.launch {
            val movie = Movie(
                imgUrl = form.imgUrl,
                title = form.title,
                desc = form.description,
                duration = form.duration,
                genre = form.genre,
                language = form.language,
                subtitles = form.subtitles,
                ageRating = form.ageRating,
                releaseDate = form.releaseDate
            )
            safeApiCall {
                movieRepo.createMovie(movie)
                _finish.emit(Unit)
            }
        }
    }
}