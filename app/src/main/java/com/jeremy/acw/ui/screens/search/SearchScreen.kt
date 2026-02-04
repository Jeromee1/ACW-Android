package com.jeremy.acw.ui.screens.search

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.ui.FieldData
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomTextField
import com.jeremy.acw.ui.components.movie.MovieItem
import com.jeremy.acw.ui.nav.Screen

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val isLoggedIn = viewModel.isLoggedIn
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val movies = viewModel.sortedMovies.collectAsStateWithLifecycle().value
    var search by remember { mutableStateOf("") }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toast.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Search(
        isLoading,
        movies,
        search,
        { search = it; viewModel.search(search) },
        { navController.navigate(Screen.Details(it, isLoggedIn)) }
    )
}

@Composable
fun Search(
    isLoading: Boolean,
    movies: List<Movie>,
    search: String,
    onSearchChanged: (String) -> Unit,
    navToDetails: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CustomTextField(FieldData("Search", search) { onSearchChanged(it) })
        if(!isLoading) {
            LazyVerticalGrid(
                GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(movies) { movie ->
                    MovieItem(movie, { navToDetails(it) }, {})
                }
            }
        } else {
            LoadingSpinner()
        }
    }
}