package com.jeremy.acw.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.repo.MovieRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repo: MovieRepo,
    savedStateHandle: SavedStateHandle
): BaseViewModel() {
    val movieId = savedStateHandle.get<String>("movieId")!!
    private val _movie = MutableStateFlow<Movie?>(null)
    val movie = _movie.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchMovie()
    }



    fun fetchMovie() {
        viewModelScope.launch {
            safeApiCall {
                val data = repo.fetchMovie(movieId)
                _movie.value = data
                _isLoading.value = false
            }
        }
    }
}