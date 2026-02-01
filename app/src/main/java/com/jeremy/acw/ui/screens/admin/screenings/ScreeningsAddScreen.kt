package com.jeremy.acw.ui.screens.admin.screenings

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.jeremy.acw.core.utils.dateFormatter
import com.jeremy.acw.core.utils.makeTimestamp
import com.jeremy.acw.core.utils.timeFormatter
import com.jeremy.acw.core.utils.timestampToTime
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.ui.FieldData
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomDialog
import com.jeremy.acw.ui.components.inputs.CustomTextField
import com.jeremy.acw.ui.components.movie.MovieItem
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.Secondary
import java.time.LocalDate
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreeningsAddScreen(
    navController: NavController,
    viewModel: ScreeningsAddViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val movies = viewModel.sortedMovies.collectAsStateWithLifecycle().value
    val hall = viewModel.hall.collectAsStateWithLifecycle().value
    val theatreId = viewModel.theatreId
    val date = viewModel.date
    val lastMovieEnd = viewModel.lastMovieEnd

    val openingTime = makeTimestamp(LocalDate.parse(date), 10, 0)

    var startingTime by remember { mutableStateOf(openingTime) }
    var search by remember { mutableStateOf("") }
    var selectedMovie by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val displayTime = timestampToTime(startingTime).format(timeFormatter())

    LaunchedEffect(lastMovieEnd) {
        startingTime =
            if (lastMovieEnd == null) {
                openingTime
            } else {
                val nextStartingTime = lastMovieEnd + 20 * 60 * 1000
                Timestamp(Date(nextStartingTime))
            }
    }

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.popBackStack(Screen.Hall::class, inclusive = false)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toast.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    fun submit() {
        if(selectedMovie.isNotBlank()) {
            viewModel.addScreening(selectedMovie, startingTime)
        }
    }

    if(hall != null) {
        ScreeningsAdd(
            isLoading,
            movies,
            theatreId,
            hall.hallName,
            LocalDate.parse(date),
            displayTime,
            search,
            { search = it; viewModel.search(it) },
            { selectedMovie = it; showDialog = true }
        )

    }

    if(showDialog) {
        CustomDialog(
            { showDialog = false },
            { submit(); showDialog = false },
            "Are you sure you want to create a screening for this movie at $displayTime",
            dialogText = "",
            Icons.Default.CheckCircleOutline
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreeningsAdd(
    isLoading: Boolean,
    movies: List<Movie>,
    theatreId: String,
    hallName: String,
    date: LocalDate,
    displayTime: String,
    search: String,
    onSearchChanged: (String) -> Unit,
    onSelectMovie: (String) -> Unit,
) {
    AdminContentWrapper("Hall - $hallName") {
        Text(
            theatreId,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            date.format(dateFormatter()),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        if(!isLoading) {
            Text(
                "Movie will be queued from $displayTime onwards",
                color = Secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            CustomTextField(FieldData("Search", search) { onSearchChanged(it) })
            if(movies.isNotEmpty()) {
                LazyVerticalGrid(
                    GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movies) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            MovieItem(
                                it,
                                { onSelectMovie(it) },
                                {}
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No movies ready :(")
                }
            }
        } else {
            LoadingSpinner()
        }
    }
}