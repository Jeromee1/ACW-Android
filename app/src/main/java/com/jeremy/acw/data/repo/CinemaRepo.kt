package com.jeremy.acw.data.repo

import com.jeremy.acw.data.model.cinema.Booking
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.model.cinema.Theatre
import com.jeremy.acw.data.model.forms.UpdateMovieForm

interface MovieRepo {
    suspend fun fetchAllMovies(): List<Movie>
    suspend fun fetchMovie(id: String): Movie
    suspend fun createMovie(movie: Movie)
    suspend fun updateMovie(id: String, form: UpdateMovieForm)
    suspend fun deleteMovie(id: String)
}

interface TheatreRepo {
    suspend fun fetchAllTheatres(): List<Theatre>
    suspend fun fetchTheatre(id: String): Theatre
    suspend fun createTheatre(theatre: String)
    suspend fun deleteTheatre(id: String)
}

interface HallRepo {
    suspend fun fetchAllHalls(theatreId: String): List<Hall>
    suspend fun fetchHall(id: String): Hall
    suspend fun createHall(hall: Hall)
    suspend fun deleteHall(id: String)
}

interface ScreeningRepo {
    suspend fun fetchAllScreenings(): List<Screening>
    suspend fun fetchScreening(id: String): Screening
    suspend fun createScreening(screening: Screening)
    suspend fun deleteScreening(id: String)
}

interface BookingRepo {
    suspend fun fetchAllBookings(): List<Booking>
    suspend fun fetchBooking(id: String): Booking
    suspend fun createBooking(booking: Booking)
    suspend fun deleteBooking(id: String)
}