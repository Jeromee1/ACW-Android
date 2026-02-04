package com.jeremy.acw.ui.screens.admin.manageSeats

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.seats.CustomSeatLayout
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun ManageSeatsScreen(
    navController: NavController,
    viewModel: ManageSeatsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val originalSeats = viewModel.seats.collectAsStateWithLifecycle()
    val editedSeats = viewModel.tempSeats.collectAsStateWithLifecycle()
    val hallSize = viewModel.hallSize
    var selectedSeat by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.popBackStack()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toast.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    ManageSeats(
        hallSize,
        originalSeats.value,
        editedSeats.value,
        selectedSeat,
        { selectedSeat = it },
        { viewModel.addToTemp(selectedSeat, it); selectedSeat = "" },
        { viewModel.submit() },
        {
            Toast.makeText(
                context,
                "No new changes yet.",
                Toast.LENGTH_SHORT
            ).show()
        },
    )
}

@Composable
fun ManageSeats(
    size: String,
    originSeatMap: Map<String, SeatStatus>,
    seatMap: Map<String, SeatStatus>,
    selectedSeat: String,
    onSeatClicked: (String) -> Unit,
    onOptionClicked: (String) -> Unit,
    submit: () -> Unit,
    showWarning: () -> Unit
) {
    AdminContentWrapper("Manage Seats") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            CustomSeatLayout(
                true,
                size,
                seatMap
            ) { onSeatClicked(it) }
        }
        HorizontalDivider(thickness = 1.dp, color = KindaWhite)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(0.6f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(selectedSeat.isNotBlank()) {
                    Text("You are editing seat $selectedSeat", fontSize = 18.sp)
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Secondary, RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            { onOptionClicked("ma") }
                        ) {
                            Icon(
                                Icons.Filled.Handyman,
                                null,
                                tint = KindaWhite,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        VerticalDivider(thickness = 1.dp, color = KindaWhite)
                        IconButton(
                            { onOptionClicked("un") }
                        ) {
                            Icon(
                                Icons.Filled.Block,
                                null,
                                tint = KindaWhite,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        VerticalDivider(thickness = 1.dp, color = KindaWhite)
                        IconButton(
                            { onOptionClicked("cl") }
                        ) {
                            Icon(
                                Icons.Filled.RemoveCircleOutline,
                                null,
                                tint = KindaWhite,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                } else {
                    Text("Select a seat")
                }
                Button(
                    onClick = {
                        if(originSeatMap != seatMap) submit()
                        else showWarning()
                    },
                    modifier = Modifier
                        .padding(40.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        if(originSeatMap != seatMap) Primary else Secondary
                    )
                ) { Text("Update") }
            }
        }
    }
}