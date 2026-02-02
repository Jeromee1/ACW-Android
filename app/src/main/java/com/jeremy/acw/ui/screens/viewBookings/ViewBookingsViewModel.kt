package com.jeremy.acw.ui.screens.viewBookings

import androidx.lifecycle.viewModelScope
import com.jeremy.acw.core.service.AuthService
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.model.cinema.Booking
import com.jeremy.acw.data.model.user.UserData
import com.jeremy.acw.data.repo.BookingRepo
import com.jeremy.acw.data.repo.ScreeningRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ViewBookingsViewModel @Inject constructor(
    private val bookingRepo: BookingRepo,
    private val screeningRepo: ScreeningRepo,
    private val auth: AuthService
) : BaseViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _user = MutableStateFlow<UserData?>(null)
    val user = _user.asStateFlow()

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings = _bookings.asStateFlow()

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            _isLoading.value = true

            val userJob = async { fetchUser() }
            val bookingsJob = async { fetchBookings() }

            userJob.await()
            bookingsJob.await()

            _isLoading.value = false
        }
    }

    suspend fun fetchUser() {
        safeApiCall {
            val firebaseUser = auth.getCurrentUser() ?: error("No authenticated user")
            auth.fetchUserData(firebaseUser.uid)
            _user.value = auth.currentUser.value ?: error("User data missing after fetch")
        }
    }

    suspend fun fetchBookings() {
        safeApiCall {
            val firebaseUser = auth.getCurrentUser() ?: error("No authenticated user")
            val fetchedBookings = bookingRepo.fetchUsersBookings(firebaseUser.uid)
                .sortedBy { it.startTime }

            _bookings.value = fetchedBookings
        }
    }

    fun cancelTicket(theatreId: String, hallId: String, screeningId: String, bookingSeats: List<String>, bookingId: String) {
        viewModelScope.launch {
            safeApiCall {
                val seatsToRemove = bookingSeats.associateWith { SeatStatus.VACANT }
                val fetchedSeats = bookingSeats.associateWith { SeatStatus.SOLD }
                screeningRepo.updateSeats(
                    theatreId,
                    hallId,
                    screeningId,
                    fetchedSeats,
                    seatsToRemove
                )
                bookingRepo.deleteBooking(bookingId)
                fetchBookings()
            }
        }
    }
}
