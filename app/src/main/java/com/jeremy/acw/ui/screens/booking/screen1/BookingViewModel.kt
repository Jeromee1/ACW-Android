package com.jeremy.acw.ui.screens.booking.screen1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.core.service.AuthService
import com.jeremy.acw.core.utils.timestampToDate
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.repo.MovieRepo
import com.jeremy.acw.data.repo.ScreeningRepo
import com.jeremy.acw.ui.screens.booking.BaseBookingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class BookingViewModel @Inject constructor(
    private val movieRepo: MovieRepo,
    private val screeningRepo: ScreeningRepo,
    private val auth: AuthService,
    savedStateHandle: SavedStateHandle
) : BaseBookingViewModel() {
    val movieId = savedStateHandle.get<String>("movieId")!!

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _sortedScreenings = MutableStateFlow<List<Screening>>(emptyList())
    val sortedScreenings = _sortedScreenings.asStateFlow()

    val screeningDates: StateFlow<List<LocalDate>> = _screenings
        .map { list ->
            list.map { timestampToDate(it.startTime!!) }.distinct().sorted()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val screeningTypes: StateFlow<List<String>> = _screenings
        .map { list ->
            list.map { it.hallType }.distinct().sorted()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            _isLoading.value = true

            val movieJob = async { fetchMovie() }
            val screeningsJob = async { fetchScreeningsForMovie() }
            val userJob = async { fetchUser() }

            movieJob.await()
            screeningsJob.await()
            userJob.await()

            _user.value = auth.currentUser.value
            _isLoading.value = false
        }
    }

    suspend fun fetchUser() {
        val firebaseUser = auth.getCurrentUser() ?: return
        auth.fetchUserData(firebaseUser.uid)
    }

    suspend fun fetchMovie() {
        safeApiCall {
            val data = movieRepo.fetchMovie(movieId)
            _movie.value = data
        }
    }

    suspend fun fetchScreeningsForMovie() {
        safeApiCall {
            val data = screeningRepo.fetchMovieScreening(movieId)
            _screenings.value = data.filter {
                    timestampToDate(it.startTime!!) >= LocalDate.now(
                    ZoneId.systemDefault()
                )
            }
            _sortedScreenings.value = screenings.value
        }
    }

    fun screeningsFilterSort(
        date: LocalDate,
        hallTypes: List<String>
    ) {
        _sortedScreenings.value =
            _screenings.value
                .filter { screening ->
                    timestampToDate(screening.startTime!!) == date &&
                            (hallTypes.isEmpty() || hallTypes.contains(screening.hallType))
                }
                .sortedBy { screening -> screening.startTime }
    }
}