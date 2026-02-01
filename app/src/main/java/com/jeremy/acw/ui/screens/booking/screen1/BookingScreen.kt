package com.jeremy.acw.ui.screens.booking.screen1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.jeremy.acw.core.utils.longToStringTimeConverter
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.ui.components.booking.BookingDateItem
import com.jeremy.acw.ui.components.booking.BookingExperienceItem
import com.jeremy.acw.ui.components.booking.BookingTheatreItem
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.core.SkeletonUI
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.BlackT
import com.jeremy.acw.ui.theme.BlackWhiteTGI
import com.jeremy.acw.ui.theme.Primary
import com.jeremy.acw.ui.theme.Secondary
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingScreen(
    navController: NavController,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val movie by viewModel.movie.collectAsStateWithLifecycle()
    val screenings by viewModel.sortedScreenings.collectAsStateWithLifecycle()
    val dates by viewModel.screeningDates.collectAsStateWithLifecycle()
    val types by viewModel.screeningTypes.collectAsStateWithLifecycle()

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTypes by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(dates) {
        if (selectedDate == null && dates.isNotEmpty()) {
            selectedDate = dates.first()
            viewModel.screeningsFilterSort(dates.first(), selectedTypes)
        }
    }

    if (!isLoading) {
        if (movie != null) {
            Booking(
                movie = movie!!,
                hasScreenings = screenings.isNotEmpty(),
                dates = dates,
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    viewModel.screeningsFilterSort(date, selectedTypes)
                },
                types = types,
                selectedTypes = selectedTypes,
                onTypeSelected = { type ->
                    selectedTypes =
                        if (selectedTypes.contains(type)) selectedTypes - type
                        else selectedTypes + type

                    viewModel.screeningsFilterSort(selectedDate!!, selectedTypes)
                },
                screenings = screenings,
                onScreeningClicked = { theatreId, hallId, screeningId ->
                    navController.navigate(Screen.BookingSeats(theatreId, hallId, screeningId, movie!!.title)
                ) }
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Movie Not Found :(")
            }
        }
    } else {
        LoadingSpinner()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Booking(
    movie: Movie,
    hasScreenings: Boolean,
    dates: List<LocalDate>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    types: List<String>,
    selectedTypes: List<String>,
    onTypeSelected: (String) -> Unit,
    screenings: List<Screening>,
    onScreeningClicked: (String, String, String) -> Unit
) {
    val screeningsByTheatre = screenings.groupBy { it.theatreId }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BlackT)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = movie.imgUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .aspectRatio(2f / 3)
                    .border(2.dp, Primary)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                loading = { SkeletonUI() }
            )
            Text(
                movie.title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(20.dp, 0.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${longToStringTimeConverter(movie.duration)} " +
                            "• ${movie.genre.firstOrNull()}",
                    fontSize = 12.sp
                )
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .border(1.dp, Secondary, RoundedCornerShape(4.dp))
                        .padding(2.dp)
                ) {
                    Text(
                        movie.ageRating,
                        fontSize = 10.sp
                    )
                }
            }
        }
        if(hasScreenings) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlackWhiteTGI)
                    .padding(20.dp)
            ) {
                Text(
                    "Select Date",
                    fontSize = 14.sp,
                    color = Secondary
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dates) {
                        BookingDateItem(it, selectedDate == it) {
                            onDateSelected(it)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text(
                    "Select Experiences",
                    fontSize = 14.sp,
                    color = Secondary
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(types) {
                        BookingExperienceItem(it, selectedTypes.contains(it)) { onTypeSelected(it) }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                screeningsByTheatre.forEach { (theatreId, theatreScreenings) ->
                    BookingTheatreItem(
                        theatreId,
                        theatreScreenings
                    ) { theatreId, hallId, screeningId ->
                        onScreeningClicked(theatreId, hallId, screeningId)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlackWhiteTGI)
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No screenings :(")
            }
        }
    }
}