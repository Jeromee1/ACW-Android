package com.jeremy.mobstarterapp.ui.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Home: Screen()
}