package com.jeremy.acw.ui.screens.search

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
class SearchViewModel @Inject constructor(
    private val movieRepo: MovieRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val isLoggedIn = savedStateHandle.get<Boolean>("isLoggedIn")!!

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
                _sortedMovies.value = data
                _isLoading.value = false
            }
        }
    }

    fun search(search: String) {
        _sortedMovies.value = movies.value.filter {
            it.title.startsWith(search, ignoreCase = true)
        }
    }
}