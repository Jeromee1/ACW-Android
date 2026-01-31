package com.jeremy.acw.ui.screens.admin.overview

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
class OverviewViewModel @Inject constructor(
    private val movieRepo: MovieRepo
) : BaseViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies = _movies.asStateFlow()

    private val _sortedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val sortedMovies = _sortedMovies.asStateFlow()

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            safeApiCall {
                val data = movieRepo.fetchAllMovies()
                _movies.value = data
                _sortedMovies.value = movies.value
                _isLoading.value = false
            }
        }
    }

    fun deleteMovie(movieId: String) {
        viewModelScope.launch {
            safeApiCall {
                movieRepo.deleteMovie(movieId)
                fetchMovies()
            }
        }
    }

    fun search(search: String) {
        _sortedMovies.value = movies.value.filter {
            it.id.startsWith(search, ignoreCase = true)
        }
    }
}