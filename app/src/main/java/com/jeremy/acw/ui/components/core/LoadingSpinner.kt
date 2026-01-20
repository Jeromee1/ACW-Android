package com.jeremy.acw.ui.components.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jeremy.acw.R
import com.jeremy.acw.ui.theme.BlackT
import com.jeremy.acw.ui.theme.Primary

@Composable
fun LoadingSpinner() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 8.dp,
            trackColor = Primary,
            modifier = Modifier.size(105.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(BlackT, RoundedCornerShape(100))
        ) {
            Image(
                painter = painterResource(R.drawable.acw_logo),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .padding(6.dp)
                    .align(Alignment.Center)
            )
        }
    }
}