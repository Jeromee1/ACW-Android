package com.jeremy.acw.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DefaultColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    onPrimary = KindaWhite,
    onSecondary = KindaWhite,
    onTertiary = KindaWhite,
    background = Background
)

@Composable
fun ACWTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DefaultColorScheme,
        typography = Typography,
        content = content
    )
}