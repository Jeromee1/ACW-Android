package com.jeremy.acw.ui.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    onClicked: () -> Unit
) {
    Button(
        onClick = { if(enabled) onClicked() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(enabled) Primary else Secondary
        ),
        modifier = modifier.fillMaxWidth()
    ) { Text(label, modifier = Modifier.padding(8.dp)) }
}