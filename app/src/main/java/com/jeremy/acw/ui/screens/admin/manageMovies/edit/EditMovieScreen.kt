package com.jeremy.acw.ui.screens.admin.manageMovies.edit

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.forms.MovieForm
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.screens.admin.manageMovies.BaseManageScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditMovieScreen(
    navController: NavController,
    viewModel: EditMovieViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val movie = viewModel.movie.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.popBackStack(Screen.Dashboard, inclusive = false)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toast.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    AdminContentWrapper("UPDATE MOVIE") {
        if(!isLoading) {
            val movieForm = MovieForm(
                imgUrl = movie!!.imgUrl,
                title = movie.title,
                description = movie.desc,
                duration = movie.duration,
                genre = movie.genre,
                language = movie.language,
                subtitles = movie.subtitles,
                ageRating = movie.ageRating,
                releaseDate = movie.releaseDate!!
            )
            BaseManageScreen(
                "UPDATE",
                movieForm,
            ) { viewModel.submit(it) }
        } else {
            LoadingSpinner()
        }
    }
}