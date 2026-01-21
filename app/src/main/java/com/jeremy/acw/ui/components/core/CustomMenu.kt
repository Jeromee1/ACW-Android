package com.jeremy.acw.ui.components.core

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.ui.theme.BlackT
import com.jeremy.acw.ui.theme.KindaWhite

@Composable
fun CustomMenu(
    isOpen: Boolean,
    onClose: () -> Unit,
    navToProfile: () -> Unit,
    navToBookings: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var showBG by remember { mutableStateOf(false) }
        val offsetX by animateDpAsState(
            targetValue = if (isOpen) 0.dp else (-300).dp,
            animationSpec = tween(250),
        )
        val opacity by animateFloatAsState(
            targetValue = if(isOpen) 1f else 0f,
            animationSpec = tween(250),
            finishedListener = { showBG = !showBG }
        )
        if(showBG) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlackT)
                    .alpha(opacity)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClose() }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight()
                .offset(x = offsetX)
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Menu",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 16.dp)
                )
                HorizontalDivider(thickness = 1.dp, color = KindaWhite)
                Text(
                    "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClose
                            navToProfile()
                        }
                        .padding(16.dp)
                )
                Text(
                    "Bookings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClose()
                            navToBookings()
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}
