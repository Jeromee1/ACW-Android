package com.jeremy.acw.ui.screens.booking.screen3

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomButton
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.BlackT
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary

@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val screening = viewModel.screening.collectAsStateWithLifecycle().value
    val seats = viewModel.selectedSeats

    var paymentOption by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.toast.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.navigate(Screen.PaymentSuccess) {
                popUpTo<Screen.Booking> { inclusive = true }
            }
        }
    }

    if(!isLoading) {
        if(screening != null) {
            Payment(
                seats,
                screening,
                paymentOption,
                { paymentOption = it }
            ) { viewModel.submit() }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Something went wrong :(")
            }
        }
    } else {
        LoadingSpinner()
    }
}

@Composable
fun Payment(
    seats: String,
    screening: Screening,
    selectedPayment: Int,
    onSelectPayment: (Int) -> Unit,
    submit: () -> Unit
) {
    val seatsList = seats.split(",")
    val selectedSeats = seatsList.joinToString(", ")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Payment",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Selected Seats:",
                    fontSize = 18.sp,
                    color = Secondary
                )
                Text(
                    if(seatsList.size >= 20) "Bro ur broke, no way u can afford this." else selectedSeats,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    "Experience: ${screening.hallType}",
                    fontSize = 18.sp,
                    color = Secondary
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Total Price:",
                    fontSize = 18.sp,
                    color = Secondary
                )
                Text(
                    "RM${screening.price} x ${seatsList.size} = RM${screening.price * seatsList.size}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = KindaWhite,
                    modifier = Modifier.padding(0.dp, 10.dp)
                )
                Text(
                    "Payment Options: ${
                        when(selectedPayment) {
                            1 -> "Credit Card"
                            2 -> "E-Wallet"
                            else -> ""
                        }}",
                    fontSize = 18.sp,
                    color = Secondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(3f / 2.2f)
                            .clickable { onSelectPayment(1) },
                        colors = CardDefaults.cardColors(
                            containerColor = BlackT
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if(selectedPayment == 1) Primary else Secondary)
                    ) { Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.CreditCard,
                            null,
                            tint = KindaWhite,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(12.dp)
                        )
                    } }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(3f / 2.2f)
                            .clickable { onSelectPayment(2) },
                        colors = CardDefaults.cardColors(
                            containerColor = BlackT
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if(selectedPayment == 2) Primary else Secondary)
                    ) { Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.BookOnline,
                            null,
                            tint = KindaWhite,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(12.dp)
                        )
                    } }
                }
            }
        }
        CustomButton(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            "Purchase",
            selectedPayment != 0
        ) { submit() }
    }
}