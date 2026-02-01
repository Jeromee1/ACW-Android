package com.jeremy.acw.ui.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Splash: Screen()
    @Serializable
    object Login: Screen()
    @Serializable
    object Register: Screen()
    @Serializable
    object Home: Screen()
    @Serializable
    data class Details(val movieId: String, val isLoggedIn: Boolean): Screen()
    @Serializable
    object Profile: Screen()
    @Serializable
    object Dashboard: Screen()
    @Serializable
    object Theatres: Screen()
    @Serializable
    data class Hall(val theatreId: String): Screen()
    @Serializable
    data class Screenings(
        val theatreId: String,
        val hallId: String,
        val hallName: String,
        val hallSize: String
    ): Screen()
    @Serializable
    data class ScreeningsAdd(
        val theatreId: String,
        val hallId: String,
        val date: String,
        val lastMovieEnd: Long?
    ): Screen()
    @Serializable
    data class ManageSeats(
        val theatreId: String,
        val hallId: String,
        val hallSize: String,
        val screeningId: String
    )
    @Serializable
    object OverviewMovies: Screen()
    @Serializable
    object AddMovie: Screen()
    @Serializable
    data class EditMovie(val movieId: String): Screen()
    @Serializable
    data class Booking(val  movieId: String): Screen()
    @Serializable
    data class BookingSeats(
        val theatreId: String,
        val hallId: String,
        val screeningId: String
    ): Screen()
}