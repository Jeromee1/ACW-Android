package com.jeremy.acw.ui.screens.admin.screenings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.jeremy.acw.core.utils.timestampToDate
import com.jeremy.acw.data.enums.cinema.HallType
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.repo.HallRepo
import com.jeremy.acw.data.repo.MovieRepo
import com.jeremy.acw.data.repo.ScreeningRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ScreeningsAddViewModel @Inject constructor(
    private val movieRepo: MovieRepo,
    private val hallRepo: HallRepo,
    private val screeningRepo: ScreeningRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val theatreId = savedStateHandle.get<String>("theatreId")!!
    val hallId = savedStateHandle.get<String>("hallId")!!
    val date = savedStateHandle.get<String>("date")!!
    val lastMovieEnd = savedStateHandle.get<Long>("lastMovieEnd")

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies = _movies.asStateFlow()

    private val _hall = MutableStateFlow<Hall?>(null)
    val hall = _hall.asStateFlow()


    private val _sortedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val sortedMovies = _sortedMovies.asStateFlow()

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            _isLoading.value = true

            val moviesJob = async { fetchAllMovies() }
            val userJob = async { fetchHall() }

            moviesJob.await()
            userJob.await()

            _isLoading.value = false
        }
    }

    fun fetchAllMovies() {
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

    suspend fun fetchMovieById(id: String): Movie {
        return movieRepo.fetchMovie(id)
    }

    fun fetchHall() {
        viewModelScope.launch {
            safeApiCall {
                val data = hallRepo.fetchHall(theatreId, hallId)
                _hall.value = data
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addScreening(
        movieId: String,
        startingTime: Timestamp
    ) {
        viewModelScope.launch {
            val intendedDay = LocalDate.parse(date)
            val startDay = timestampToDate(startingTime)

            if (startDay.isAfter(intendedDay)) {
                _toast.emit("Cannot add: The schedule for this day is already full.")
                return@launch
            }

            val movie = fetchMovieById(movieId)
            val endTime = Timestamp(Date(startingTime.toDate().time + (movie.duration * 1000L)))

            val newScreening = Screening(
                movieId = movieId,
                movieTitle = movie.title,
                theatreId = theatreId,
                hallId = hallId,
                hallName = hall.value!!.hallName,
                hallType = hall.value!!.hallType,
                startTime = startingTime,
                endTime = endTime,
                price = if(hall.value?.hallType == HallType.TwoD.value) 20.0 else 45.0
            )

            safeApiCall {
                screeningRepo.createScreening(theatreId, hallId, newScreening)
                _finish.emit(Unit)
            }
        }
    }

    fun search(search: String) {
        _sortedMovies.value = movies.value.filter {
            it.title.startsWith(search, ignoreCase = true)
        }
    }
}