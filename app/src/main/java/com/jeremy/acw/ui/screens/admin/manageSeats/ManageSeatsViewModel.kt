package com.jeremy.acw.ui.screens.admin.manageSeats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.repo.ScreeningRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ManageSeatsViewModel @Inject constructor(
    val screeningRepo: ScreeningRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val theatreId = savedStateHandle.get<String>("theatreId")!!
    val hallId = savedStateHandle.get<String>("hallId")!!
    val screeningId = savedStateHandle.get<String>("screeningId")!!
    val hallSize = savedStateHandle.get<String>("hallSize")!!

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _seats = MutableStateFlow<Map<String, SeatStatus>>(emptyMap())
    val seats = _seats.asStateFlow()

    private val _tempSeats = MutableStateFlow<Map<String, SeatStatus>>(emptyMap())
    val tempSeats = _tempSeats.asStateFlow()

    init {
        fetchSeats()
    }

    fun fetchSeats() {
        viewModelScope.launch {
            _isLoading.value = true
            safeApiCall {
                val data = screeningRepo.fetchScreening(theatreId, hallId, screeningId)
                _seats.value = data.seats.mapValues {
                    SeatStatus.fromString(it.value)
                }
                _tempSeats.value = seats.value
                _isLoading.value = false
            }
        }
    }

    fun submit() {
        viewModelScope.launch {
            safeApiCall {
                screeningRepo.updateSeats(
                    theatreId,
                    hallId,
                    screeningId,
                    _seats.value,
                    tempSeats.value
                )
                _finish.emit(Unit)
            }
        }
    }

    fun addToTemp(seat: String, option: String) {
        _tempSeats.update {
            when (option) {
                "cl" -> it + (seat to SeatStatus.VACANT)
                "ma" -> it + (seat to SeatStatus.MAINTENANCE)
                "un" -> it + (seat to SeatStatus.UNAVAILABLE)
                else -> it
            }
        }
    }
}