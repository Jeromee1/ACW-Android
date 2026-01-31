package com.jeremy.acw.ui.screens.admin.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.ui.FieldData
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.core.CustomBottomSheet
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomDialog
import com.jeremy.acw.ui.components.inputs.CustomTextField
import com.jeremy.acw.ui.components.movie.MovieItem
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.Danger
import com.jeremy.acw.ui.theme.KindaWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    navController: NavController,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val movies = viewModel.sortedMovies.collectAsStateWithLifecycle().value
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDialog by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    Overview(
        isLoading,
        movies,
        search,
        { viewModel.search(it) },
        { navController.navigate(Screen.EditMovie(it)) },
        { selectedMovie = it; scope.launch { sheetState.show() }},
        { navController.navigate(Screen.AddMovie) }
    )
    selectedMovie?.let {
        CustomBottomSheet(
            sheetState,
            { scope.launch { sheetState.hide() } }
        ) {
            Button(
                onClick = { showDialog = true; scope.launch { sheetState.hide() } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Danger
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) { Text("Delete") }
        }
        if(showDialog) {
            CustomDialog(
                { showDialog = false },
                { viewModel.deleteMovie(selectedMovie!!.id); showDialog = false },
                "Delete Movie?",
                "Are you sure you want to delete ${selectedMovie!!.title}?",
                icon = Icons.Filled.Warning
            )
        }
    }
}

@Composable
fun Overview(
    isLoading: Boolean,
    movies: List<Movie>,
    search: String,
    onSearchChanged: (String) -> Unit,
    onClicked: (String) -> Unit,
    onLongPressed: (Movie) -> Unit,
    navToAdd: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        AdminContentWrapper("MOVIES") {
            if(!isLoading) {
                CustomTextField(FieldData("Search", search) { onSearchChanged(it) })
                if(movies.isNotEmpty()) {
                    LazyVerticalGrid(
                        GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                       items(movies) { movie ->
                           Box(
                               modifier = Modifier.weight(1f),
                               contentAlignment = Alignment.Center
                           ) {
                               MovieItem(
                                   movie,
                                   { onClicked(it) },
                                   { onLongPressed(movie) }
                               )
                           }
                       }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("No movies here.", fontSize = 20.sp, fontWeight = FontWeight.Bold) }
                }
            } else {
                LoadingSpinner()
            }
        }
        Button(
            onClick = { navToAdd() },
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth()
                .align(Alignment.BottomEnd),
            shape = RoundedCornerShape(8.dp)
        ) { Icon(
            Icons.Default.Add,
            null,
            tint = KindaWhite,
            modifier = Modifier.size(30.dp)
        ) }
    }
}