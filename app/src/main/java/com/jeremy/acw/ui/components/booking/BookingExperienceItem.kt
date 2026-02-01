package com.jeremy.acw.ui.components.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary
import com.jeremy.acw.ui.theme.SecondarySecondary

@Composable
fun BookingExperienceItem(
    type: String,
    selected: Boolean = false,
    onClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onClicked() },
        colors = CardDefaults.cardColors(
            containerColor = if(selected) Primary else SecondarySecondary
        ),
        border = BorderStroke(1.dp, Secondary),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                type,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}