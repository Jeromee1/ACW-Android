package com.jeremy.acw.ui.screens.booking

import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.model.user.UserData
import com.jeremy.acw.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseBookingViewModel: BaseViewModel() {
    protected val _user = MutableStateFlow<UserData?>(null)
    val user = _user.asStateFlow()

    protected val _movie = MutableStateFlow<Movie?>(null)
    val movie = _movie.asStateFlow()

    protected val _screenings = MutableStateFlow<List<Screening>>(emptyList())
    val screenings = _screenings.asStateFlow()

    protected val _hall = MutableStateFlow<Hall?>(null)
    val hall = _hall.asStateFlow()

    protected val _screening = MutableStateFlow<Screening?>(null)
    val screening = _screening.asStateFlow()

    protected val _tempSeats = MutableStateFlow<Map<String, SeatStatus>>(emptyMap())
    val tempSeats = _tempSeats.asStateFlow()
}