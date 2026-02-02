package com.jeremy.acw.ui.screens.viewBookings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jeremy.acw.data.model.cinema.Booking
import com.jeremy.acw.ui.components.booking.ViewBookingsItem
import com.jeremy.acw.ui.components.core.CustomBottomSheet
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomButton
import com.jeremy.acw.ui.components.inputs.CustomDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewBookingsScreen(
    viewModel: ViewBookingsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val bookings = viewModel.bookings.collectAsStateWithLifecycle().value

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedBooking by remember { mutableStateOf<Booking?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    if(!isLoading) {
        ViewBookings(
            bookings,
        ) { selectedBooking = it; scope.launch { sheetState.show() } }
    } else {
        LoadingSpinner()
    }

    selectedBooking?.let { booking ->
        CustomBottomSheet(
            sheetState,
            { scope.launch { sheetState.hide() } }
        ) {
            CustomButton(
                label = "Cancel Ticket"
            ) {
                showDialog = true
            }
        }
    }

    if(showDialog) {
        CustomDialog(
            { showDialog = false },
            {
                viewModel.cancelTicket(
                    selectedBooking!!.theatreId,
                    selectedBooking!!.hallId,
                    selectedBooking!!.screeningId,
                    selectedBooking!!.seats,
                    selectedBooking!!.id
                )
                showDialog = false
                scope.launch { sheetState.hide() }
                selectedBooking = null
            },
            "Cancel Ticket?",
            "You will be eliminated if we find you.",
            Icons.Filled.Cancel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewBookings(
    bookings: List<Booking>,
    onItemSelected: (Booking) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(bookings) {
            ViewBookingsItem(it) { onItemSelected(it) }
        }
    }
}