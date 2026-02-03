package com.jeremy.acw.ui.screens.booking.screen4

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.jeremy.acw.ui.nav.Screen
import kotlinx.coroutines.delay

@Composable
fun PaymentSuccessScreen(
    navController: NavController
) {
    LaunchedEffect(Unit) {
        delay(4000)
        navController.navigate(Screen.Home) {
            popUpTo(Screen.PaymentSuccess) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Thank you for your purchase!\nRedirecting you soon...",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}