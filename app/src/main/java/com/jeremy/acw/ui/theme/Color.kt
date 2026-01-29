package com.jeremy.acw.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Primary = Color(109, 40, 217)
val Secondary = Color(107, 114, 128)
val Background = Color(36, 35, 42)
val KindaWhite = Color(240, 240, 240)
val BlackT = Color(0, 0, 0, 100)
val Gray = Color(180, 180, 180)

val BlackTG = Brush.linearGradient(
    colors = listOf(
        Color(0, 0, 0, 0),
        Color(0, 0, 0, 100),
        Color(0, 0, 0, 150),
        Color(0, 0, 0, 175),
        Color(0, 0, 0, 200),
        Color(0, 0, 0, 225),
        Color(0, 0, 0, 255)
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)

val BlackPurpleTG = Brush.linearGradient(
    colors = listOf(
        Color(109, 40, 217, 100),
        Color(40, 10, 90, 100),
        BlackT, BlackT
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)

val Success = Color(0, 120, 0)
val Danger = Color(150, 0, 0)
