package com.jeremy.acw.ui.screens.booking.screen2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.core.utils.seatMapToString
import com.jeremy.acw.core.utils.seatToString
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomButton
import com.jeremy.acw.ui.components.seats.CustomSeatLayout
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.BlackT
import com.jeremy.acw.ui.theme.Secondary
import com.jeremy.acw.ui.theme.SecondarySecondary

@Composable
fun BookingSeatSelectScreen(
    navController: NavController,
    viewModel: BookingSeatSelectViewmodel = hiltViewModel()
) {
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val hall = viewModel.hall.collectAsStateWithLifecycle().value
    val screening = viewModel.screening.collectAsStateWithLifecycle().value
    val originalSeats = viewModel.seats.collectAsStateWithLifecycle().value
    val seats = viewModel.tempSeats.collectAsStateWithLifecycle().value
    val theatreId = viewModel.theatreId
    val hallId = viewModel.hallId
    val screeningId = viewModel.screeningId
    val movieTitle = viewModel.movieTitle

    val selectedSeats = remember { mutableStateListOf<String>() }

    if (!isLoading && hall != null) {
        BookingSeatSelect(
            screening!!,
            hall = hall,
            seatMap = seats,
            selectedSeats,
            onSeatSelect = {
                if (selectedSeats.contains(it)) selectedSeats.remove(it)
                else selectedSeats.add(it)
                viewModel.onSeatClicked(selectedSeats.toList())
            }
        ) {
            navController.navigate(
                Screen.Payment(
                    theatreId,
                    hallId,
                    screeningId,
                    seatMapToString(originalSeats),
                    seatToString(selectedSeats),
                    movieTitle)
            )
        }
    } else {
        LoadingSpinner()
    }
}
@Composable
fun BookingSeatSelect(
    screening: Screening,
    hall: Hall,
    seatMap: Map<String, SeatStatus>,
    selectedSeats: List<String>,
    onSeatSelect: (String) -> Unit,
    navToPurchase: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select Seats",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlackT)
                    .padding(20.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .background(BlackT, RoundedCornerShape(12.dp))
            ) {
                CustomSeatLayout(
                    size = hall.seatLayout,
                    seatMap = seatMap
                ) { onSeatSelect(it) }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Selected:",
                    color = Secondary
                )
                if(selectedSeats.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(selectedSeats) {
                                Text(
                                    it,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(SecondarySecondary, RoundedCornerShape(8.dp))
                                        .padding(12.dp, 10.dp)
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Total Price:",
                            color = Secondary
                        )
                        Text(
                            "RM${selectedSeats.size * screening.price}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text("None selected")
                }
            }
        }
        CustomButton(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp),
                if(selectedSeats.isNotEmpty()) "Proceed" else "Select a seat",
            selectedSeats.isNotEmpty(),
        ) { navToPurchase() }
    }
}