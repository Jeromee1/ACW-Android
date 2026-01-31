package com.jeremy.acw.ui.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun CustomChip(
    label: String,
    isSelected: Boolean,
    onClicked: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary else Secondary
        ),
        border = BorderStroke(1.dp, if (isSelected) Color.Transparent else Color.Gray),
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable { onClicked() }
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}