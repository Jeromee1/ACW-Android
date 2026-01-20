package com.jeremy.acw.ui.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Splash: Screen()
    @Serializable
    object Login: Screen()
    @Serializable
    object Home: Screen()
}