package com.jeremy.acw.ui.components.inputs

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (() -> Unit)? = null,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    showConfirmation: Boolean = true
) {
    AlertDialog(
        icon = {
            Icon(
                icon,
                "",
                modifier = Modifier.size(50.dp)
            )
        },
        title = {
            Text(dialogTitle)
        },
        text = {
            Text(dialogText)
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            if (showConfirmation && onConfirmation != null) {
                TextButton(onClick = onConfirmation) {
                    Text("Confirm")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}