package com.jeremy.acw.ui.screens.booking.screen3

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.core.service.AuthService
import com.jeremy.acw.core.utils.seatToList
import com.jeremy.acw.core.utils.seatToMap
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.model.cinema.Booking
import com.jeremy.acw.data.repo.BookingRepo
import com.jeremy.acw.data.repo.ScreeningRepo
import com.jeremy.acw.ui.screens.booking.BaseBookingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val screeningRepo: ScreeningRepo,
    private val bookingRepo: BookingRepo,
    private val auth: AuthService,
    savedStateHandle: SavedStateHandle
) : BaseBookingViewModel() {
    val theatreId = savedStateHandle.get<String>("theatreId")!!
    val hallId = savedStateHandle.get<String>("hallId")!!
    val screeningId = savedStateHandle.get<String>("screeningId")!!
    val fetchedSeats = savedStateHandle.get<String>("fetchedSeats")!!
    val selectedSeats = savedStateHandle.get<String>("selectedSeats")!!
    val movieTitle = savedStateHandle.get<String>("movieTitle")!!

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            _isLoading.value = true

            val screeningsJob = async { fetchScreening() }
            val userJob = async { fetchUser() }

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

    fun fetchScreening() {
        viewModelScope.launch {
            safeApiCall {
                val data = screeningRepo.fetchScreening(theatreId, hallId, screeningId)
                _screening.value = data
            }
        }
    }

    fun submit() {
        val currentUser = user.value ?: return
        val currentScreening = screening.value ?: return
        val totalPrice = selectedSeats.split(",").size * currentScreening.price
        val fetchedSeats = seatToMap(fetchedSeats)
        val selectedSeats = seatToList(selectedSeats)

        viewModelScope.launch {
            val booking = Booking(
                uid = currentUser.uid,
                theatreId = theatreId,
                screeningId = screeningId,
                movieTitle = movieTitle,
                hallId = hallId,
                hallName = currentScreening.hallName,
                startTime = currentScreening.startTime,
                endTime = currentScreening.endTime,
                seats = selectedSeats,
                totalPrice = totalPrice
            )
            safeApiCall {
                screeningRepo.updateSeats(
                    theatreId,
                    hallId,
                    screeningId,
                    fetchedSeats,
                    fetchedSeats + selectedSeats.associateWith { SeatStatus.SOLD }
                )
                bookingRepo.createBooking(booking)
                _finish.emit(Unit)
            }
        }
    }
}