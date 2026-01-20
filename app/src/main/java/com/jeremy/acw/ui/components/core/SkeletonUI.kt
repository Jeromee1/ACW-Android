package com.jeremy.acw.ui.components.core

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.jeremy.acw.ui.theme.Gray
import com.jeremy.acw.ui.theme.KindaWhite

@Composable
fun SkeletonUI() {
    val shimmerColors = listOf(
        Gray,
        KindaWhite,
        Gray
    )
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing)
        )
    )
    val colors = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value - 1000f, 0f),
        end = Offset(translateAnim.value, 0f)
    )
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(colors)
    )
}