package com.jeremy.acw.ui.components.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jeremy.acw.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    bottomSheetState: SheetState,
    closeBottomSheet: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    if(bottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { closeBottomSheet() },
            containerColor = Background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}