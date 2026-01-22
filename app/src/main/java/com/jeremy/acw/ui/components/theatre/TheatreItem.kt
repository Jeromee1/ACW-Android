package com.jeremy.acw.ui.components.theatre

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.data.model.cinema.Theatre
import com.jeremy.acw.ui.theme.Delete
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun TheatreItem(
    theatre: Theatre,
    screenings: Int = 0,
    onClicked: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked() },
        colors = CardDefaults.cardColors(containerColor = Secondary),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    theatre.id,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text("Screenings: $screenings", fontSize = 15.sp)
            }
            if(screenings < 1) {
                IconButton(
                    onClick = { onDelete() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Delete),
                    shape = RoundedCornerShape(8.dp)
                ) { Icon(Icons.Filled.Delete, null) }
            }
        }
    }
}