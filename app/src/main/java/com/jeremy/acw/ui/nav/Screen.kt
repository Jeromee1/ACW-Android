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
    data class Screenings(val theatreId: String, val hallId: String, val hallName: String): Screen()
}