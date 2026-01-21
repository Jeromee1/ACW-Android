package com.jeremy.acw.ui.components.movie

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeremy.acw.ui.theme.Gray

@Composable
fun DetailsInfo(
    category: String,
    data: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(category, color = Gray)
        Text(data)
    }
}