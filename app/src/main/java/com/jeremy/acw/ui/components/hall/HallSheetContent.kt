package com.jeremy.acw.ui.components.hall

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremy.acw.data.enums.cinema.HallSize
import com.jeremy.acw.data.enums.cinema.HallType
import com.jeremy.acw.ui.components.inputs.CustomButton
import com.jeremy.acw.ui.components.inputs.CustomDropdown
import com.jeremy.acw.ui.theme.KindaWhite

@Composable
fun HallSheetContent(
    size: String,
    type: String,
    onSubmit: (String, String) -> Unit
) {
    var selectedSize by remember { mutableStateOf(size) }
    var selectedType by remember { mutableStateOf(type) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Hall Size",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = KindaWhite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                CustomDropdown(
                    items = HallSize.entries.map { it.value },
                    selectedItem = selectedSize,
                    itemLabel = { it }
                ) { selectedSize = it }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Hall Type",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = KindaWhite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                CustomDropdown(
                    items = HallType.entries.map { it.value },
                    selectedItem = selectedType,
                    itemLabel = { it }
                ) { selectedType = it }
            }
            CustomButton(label = "Apply Changes") { onSubmit(selectedSize, selectedType) }
        }
    }
}