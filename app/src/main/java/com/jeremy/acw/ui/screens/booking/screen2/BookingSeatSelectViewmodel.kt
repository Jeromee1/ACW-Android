package com.jeremy.acw.ui.screens.booking.screen2

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.repo.HallRepo
import com.jeremy.acw.data.repo.ScreeningRepo
import com.jeremy.acw.ui.screens.booking.BaseBookingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.plus

@HiltViewModel
class BookingSeatSelectViewmodel @Inject constructor(
    private val screeningRepo: ScreeningRepo,
    private val hallRepo: HallRepo,
    savedStateHandle: SavedStateHandle
) : BaseBookingViewModel() {
    val theatreId = savedStateHandle.get<String>("theatreId")!!
    val hallId = savedStateHandle.get<String>("hallId")!!
    val screeningId = savedStateHandle.get<String>("screeningId")!!
    val movieTitle = savedStateHandle.get<String>("movieTitle")!!

    private val _hall = MutableStateFlow<Hall?>(null)
    val hall = _hall.asStateFlow()

    private val _seats = MutableStateFlow<Map<String, SeatStatus>>(emptyMap())
    val seats = _seats.asStateFlow()

    private val _tempSeats = MutableStateFlow<Map<String, SeatStatus>>(emptyMap())
    val tempSeats = _tempSeats.asStateFlow()

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            _isLoading.value = true

            val screeningsJob = async { fetchScreening() }
            val hallJob = async { fetchHall() }

            screeningsJob.await()
            hallJob.await()

            _isLoading.value = false
        }
    }

    suspend fun fetchScreening() {
        safeApiCall {
            val data = screeningRepo.fetchScreening(theatreId, hallId, screeningId)
            _screening.value = data
            _seats.value = data.seats.mapValues { SeatStatus.fromString(it.value) }
            _tempSeats.value = data.seats.mapValues { SeatStatus.fromString(it.value) }
        }
    }

    suspend fun fetchHall() {
        safeApiCall {
            val data = hallRepo.fetchHall(theatreId, hallId)
            _hall.value = data
        }
    }

    fun onSeatClicked(seats: List<String>) {
        _tempSeats.update { currentMap ->
            val resetMap = currentMap.mapValues { (_, status) ->
                if (status == SeatStatus.BOOKED) SeatStatus.VACANT else status
            }
            resetMap + seats.map { it to SeatStatus.BOOKED }
        }
    }
}