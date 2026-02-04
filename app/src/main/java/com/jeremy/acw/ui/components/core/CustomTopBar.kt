package com.jeremy.acw.ui.components.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jeremy.acw.R
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.BlackPurpleTG
import com.jeremy.acw.ui.theme.KindaWhite

@Composable
fun CustomTopBar(
    navController: NavController,
    showBackBtn: Boolean = true,
    showMenu: Boolean = false,
    onMenuClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 1.dp)
            .background(BlackPurpleTG)
            .drawBehind {
                drawLine(
                    color = KindaWhite,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(
                20.dp,
                16.dp,
                20.dp,
                0.dp
            )
    ) {
        if(showBackBtn) {
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                "",
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterStart)
                    .clickable { navController.popBackStack() }
            )
        }
        if(showMenu) {
            Icon(
                Icons.Default.Menu,
                "",
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.CenterStart)
                    .clickable { onMenuClicked() },
                tint = KindaWhite
            )
        }
        Image(
            painter = painterResource(R.drawable.acw_logo),
            contentDescription = "",
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = true }
                    }
                }
        )
    }
}