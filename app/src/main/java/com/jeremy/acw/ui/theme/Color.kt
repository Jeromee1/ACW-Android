package com.jeremy.acw.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Primary = Color(109, 40, 217)
val Secondary = Color(107, 114, 128)
val Background = Color(36, 35, 42)
val KindaWhite = Color(240, 240, 240)
val BlackT = Color(0, 0, 0, 100)

val BlackTG = Brush.linearGradient(
    colors = listOf(
        Color(109, 40, 217, 100),
        Color(40, 10, 90, 100),
        BlackT, BlackT, BlackT, BlackT
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)