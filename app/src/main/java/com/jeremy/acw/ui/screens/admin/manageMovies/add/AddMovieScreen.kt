package com.jeremy.acw.ui.screens.admin.manageMovies.add

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.screens.admin.manageMovies.BaseManageScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddMovieScreen(
    navController: NavController,
    viewModel: AddMovieViewModel = hiltViewModel()
) {
    val context = LocalContext.current

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

    AdminContentWrapper("ADD MOVIE") {
        BaseManageScreen(
            "ADD"
        ) { viewModel.submit(it) }
    }
}