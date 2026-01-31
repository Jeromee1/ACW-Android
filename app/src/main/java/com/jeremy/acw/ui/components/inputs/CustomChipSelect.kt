package com.jeremy.acw.ui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.ui.theme.Background

@Composable
fun <T> CustomChipSelect(
    title: String,
    selectedItems: List<T>,
    items: List<T>,
    label: (T) -> String,
    onItemSelect: (T) -> Unit
) {
    val unselectedItems = items.filter { it !in selectedItems }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            title,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Selected")
        if(selectedItems.isNotEmpty()) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(selectedItems) { item ->
                    CustomChip(
                        label = label(item),
                        isSelected = true,
                        onClicked = { onItemSelect(item) }
                    )
                }
            }
        } else {
            Text(
                "None Selected",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text("Available")
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(unselectedItems) { item ->
                CustomChip(
                    label = label(item),
                    isSelected = false,
                    onClicked = { onItemSelect(item) }
                )
            }
        }
    }
}