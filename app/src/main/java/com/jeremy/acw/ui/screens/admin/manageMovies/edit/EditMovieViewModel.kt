package com.jeremy.acw.ui.screens.admin.manageMovies.edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.forms.MovieForm
import com.jeremy.acw.data.repo.MovieRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class EditMovieViewModel @Inject constructor(
    private val movieRepo: MovieRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val movieId = savedStateHandle.get<String>("movieId")!!

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie = _movie.asStateFlow()

    init {
        fetchMovie()
    }

    fun fetchMovie() {
        viewModelScope.launch {
            _isLoading.value = true
            safeApiCall {
                val data = movieRepo.fetchMovie(movieId)
                _movie.value = data
                _isLoading.value = false
            }
        }
    }

    fun submit(form: MovieForm) {
        if(!validateMovie(form)) return
        viewModelScope.launch {
            safeApiCall {
                movieRepo.updateMovie(movieId, form)
                emitToast("Success")
                _finish.emit(Unit)
            }
        }
    }
}