package com.jeremy.acw.ui.screens.admin.screenings

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.core.utils.dateFormatter
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.screenings.ScreeningItem
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreeningsScreen(
    navController: NavController,
    viewModel: ScreeningsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenings = viewModel.filteredScreenings.collectAsStateWithLifecycle().value
    val hallName = viewModel.hallName ?: ""
    val currentDate = LocalDate.now()
    val selectedDate = remember { mutableStateOf(currentDate) }
    val theatreId = viewModel.theatreId
    val hallId = viewModel.hallId

    LaunchedEffect(viewModel.screenings, selectedDate.value) {
        viewModel.filterScreenings(selectedDate.value)
    }

    Screenings(
        screenings,
        hallName,
        selectedDate.value,
        onDateChanged = { selectedDate.value = it; viewModel.filterScreenings(selectedDate.value) },
        selectedDate.value.isAfter(currentDate),
        {
            Toast.makeText(
            context,
            "Screenings can only be added the next day.",
            Toast.LENGTH_SHORT
            ).show()
        }
    ) { navController.navigate(
        Screen.ScreeningsAdd(
            theatreId,
            hallId,
            selectedDate.value.toString(),
            screenings.lastOrNull()?.endTime?.toDate()?.time
        )
    ) }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Screenings(
    screenings: List<Screening>,
    hallName: String,
    selectedDate: LocalDate,
    onDateChanged: (LocalDate) -> Unit,
    showAdd: Boolean,
    showWarning: () -> Unit,
    navToAdd: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val newDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateChanged(newDate)
                    }
                    showDatePicker = false
                    } ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AdminContentWrapper("Hall $hallName - SCREENINGS") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ChevronLeft,
                    null,
                    tint = KindaWhite,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onDateChanged(selectedDate.minusDays(1)) }
                )
                Text(
                    text = selectedDate.format(dateFormatter()),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        showDatePicker = true
                    }
                )
                Icon(
                    Icons.Default.ChevronRight,
                    null,
                    tint = KindaWhite,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onDateChanged(selectedDate.plusDays(1)) }
                )
            }
            if(screenings.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(screenings) { ScreeningItem(it) }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No movies being screened today :(")
                }
            }
        }
        Button(
            onClick = {
                if(showAdd) navToAdd()
                else showWarning()
            },
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth()
                .align(Alignment.BottomEnd),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                if(showAdd) Primary else Secondary
            )
        ) { Icon(
            Icons.Default.Add,
            null,
            tint = KindaWhite,
            modifier = Modifier.size(30.dp)
        ) }
    }
}