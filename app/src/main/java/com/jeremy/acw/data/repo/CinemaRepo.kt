package com.jeremy.acw.data.repo

import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.model.cinema.Booking
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.model.cinema.Theatre
import com.jeremy.acw.data.model.forms.MovieForm

interface MovieRepo {
    suspend fun fetchAllMovies(): List<Movie>
    suspend fun fetchMovie(id: String): Movie
    suspend fun createMovie(movie: Movie)
    suspend fun updateMovie(id: String, form: MovieForm)
    suspend fun deleteMovie(id: String)
}

interface TheatreRepo {
    suspend fun fetchAllTheatres(): List<Theatre>
    suspend fun fetchTheatre(id: String): Theatre
    suspend fun fetchScreeningsFromTheatre(id: String): Int
    suspend fun createTheatre(theatre: String)
    suspend fun deleteTheatre(id: String)
}

interface HallRepo {
    suspend fun fetchAllHalls(theatreId: String): List<Hall>
    suspend fun fetchHall(theatreId: String, id: String): Hall
    suspend fun updateHall(theatreId: String, id: String, hall: Hall)
    suspend fun deleteHall(theatreId: String, id: String)
}

interface ScreeningRepo {
    suspend fun fetchAllScreenings(theatreId: String, hallId: String): List<Screening>
    suspend fun fetchScreening(theatreId: String, hallId: String,  id: String): Screening
    suspend fun createScreening(theatreId: String, hallId: String, screening: Screening)
    suspend fun deleteScreening(theatreId: String, hallId: String, id: String)
    suspend fun updateSeats(
        theatreId: String,
        hallId: String,
        id: String,
        fetched: Map<String, SeatStatus>,
        seats: Map<String, SeatStatus>
    )
    suspend fun fetchMovieScreening(movieId: String): List<Screening>
}

interface BookingRepo {
    suspend fun fetchAllBookings(): List<Booking>
    suspend fun fetchBooking(id: String): Booking
    suspend fun createBooking(booking: Booking)
    suspend fun deleteBooking(id: String)
}