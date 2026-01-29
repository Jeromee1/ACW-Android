package com.jeremy.acw.ui.screens.admin.halls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.core.CustomBottomSheet
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.hall.HallItem
import com.jeremy.acw.ui.components.hall.HallSheetContent
import com.jeremy.acw.ui.nav.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HallScreen(
    navController: NavController,
    viewModel: HallViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val halls by viewModel.halls.collectAsStateWithLifecycle()
    var selectedItem by remember { mutableStateOf<Hall?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val theatreId = viewModel.theatreId

    Hall(isLoading, halls,
        { selectedItem = it; scope.launch { sheetState.show() } }
    ) { id, name, size ->
        navController.navigate(Screen.Screenings(theatreId, id, name, size))
    }
    selectedItem?.let {
        CustomBottomSheet(
            sheetState,
            { scope.launch { sheetState.hide() } }
        ) {
            HallSheetContent(
                selectedItem!!.seatLayout,
                selectedItem!!.hallType
            ) { size, type ->
                val updatedHall = selectedItem!!.copy(seatLayout = size, hallType = type)
                viewModel.updateHall(selectedItem!!.id, updatedHall)
                scope.launch { sheetState.hide() }
            }
        }
    }
}

@Composable
fun Hall(
    isLoading: Boolean,
    halls: List<Hall>,
    onLongPressed: (Hall) -> Unit,
    onClicked: (String, String, String) -> Unit
) {
    AdminContentWrapper("HALLS") {
        Text(
            if(halls.isNotEmpty()) halls.first().theatreId else "",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        if(!isLoading) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(halls) {
                    HallItem(
                        it,
                        { onLongPressed(it) }
                    ) { onClicked(it.id, it.hallName, it.seatLayout) }
                }
            }
        } else {
            LoadingSpinner()
        }
    }
}