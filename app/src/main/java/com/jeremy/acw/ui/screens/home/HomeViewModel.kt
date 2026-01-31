package com.jeremy.acw.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.jeremy.acw.core.service.AuthService
import com.jeremy.acw.data.enums.cinema.MovieStatus
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.user.UserData
import com.jeremy.acw.data.repo.MovieRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repo: MovieRepo,
    val auth: AuthService
): BaseViewModel() {
    private val _user = MutableStateFlow<UserData?>(null)
    val user = _user.asStateFlow()
    private val _moviesShowing = MutableStateFlow<List<Movie>>(emptyList())
    val moviesShowing = _moviesShowing.asStateFlow()

    private val _moviesSoon = MutableStateFlow<List<Movie>>(emptyList())
    val moviesSoon = _moviesSoon.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            _isLoading.value = true

            val moviesJob = async { fetchAllMovies() }
            val userJob = async { fetchUser() }

            moviesJob.await()
            userJob.await()

            _user.value = auth.currentUser.value
            _isLoading.value = false
        }
    }

    suspend fun fetchUser() {
        val firebaseUser = auth.getCurrentUser() ?: return
        auth.fetchUserData(firebaseUser.uid)
    }

    suspend fun fetchAllMovies() {
        safeApiCall {
            repo.fetchAllMovies().let {
                _moviesShowing.value = it.filter { it.status == MovieStatus.NOW_SHOWING.value }
                _moviesSoon.value = it.filter { it.status == MovieStatus.COMING_SOON.value }
            }
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        fetchAll()
        _isRefreshing.value = false
    }

}