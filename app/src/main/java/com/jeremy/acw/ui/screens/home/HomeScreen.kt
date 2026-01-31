package com.jeremy.acw.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.enums.cinema.MovieStatus
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.movie.MovieBannerItem
import com.jeremy.acw.ui.components.movie.MovieItem
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Gray

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val moviesShowing by viewModel.moviesShowing.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val moviesSoon by viewModel.moviesSoon.collectAsStateWithLifecycle()
    var selectedStatus by remember { mutableStateOf(MovieStatus.entries.first().value) }
    val refreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullState = rememberPullToRefreshState()

    if(isLoading) {
        LoadingSpinner()
    } else {
        Home(
            moviesShowing,
            moviesSoon,
            user != null,
            refreshing,
            pullState,
            { viewModel.refresh() },
            selectedStatus,
            { selectedStatus = it }
        ) { navController.navigate(Screen.Details(it, user != null)) }
    }
}

@Composable
fun Home(
    moviesShowing: List<Movie>,
    moviesSoon: List<Movie>,
    isLoggedIn: Boolean,
    refreshing: Boolean,
    refreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    selectedStatus: String,
    onSelectStatus: (String) -> Unit,
    navToDetails: (String) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = refreshing,
        state = refreshState,
        onRefresh = { onRefresh() },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(moviesShowing.isNotEmpty()) {
                MovieBannerItem(
                    moviesShowing,
                    isLoggedIn,
                    { navToDetails(it) }
                ) {  }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp, 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MovieStatus.entries.forEach {
                        Text(
                            it.value,
                            fontSize = if(selectedStatus == it.value) 20.sp else 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if(selectedStatus == it.value) KindaWhite else Gray,
                            modifier = Modifier.clickable { onSelectStatus(it.value) }
                        )
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = KindaWhite)
                val movies = if(selectedStatus == MovieStatus.NOW_SHOWING.value) moviesShowing else moviesSoon
                val columns = 3
                val rows = movies.chunked(columns)
                if(movies.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp, 0.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { movie ->
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        MovieItem(
                                            movie,
                                            { navToDetails(it) },
                                            {}
                                        )
                                    }
                                }
                                if (rowItems.size < columns) {
                                    repeat(columns - rowItems.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
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
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}