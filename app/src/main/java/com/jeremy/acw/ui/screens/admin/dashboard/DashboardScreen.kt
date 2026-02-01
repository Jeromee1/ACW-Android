package com.jeremy.acw.ui.screens.admin.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.inputs.CustomButton
import com.jeremy.acw.ui.nav.Screen

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    LaunchedEffect(viewModel.perms) {
        if(!viewModel.perms) {
            navController.navigate(Screen.Home) {
                popUpTo(Screen.Dashboard) {
                    inclusive = true
                }
            }
        }
    }

    Dashboard(
        { navController.navigate(Screen.OverviewMovies) },
        { navController.navigate(Screen.Theatres) }
    )
}

@Composable
fun Dashboard(
    navToMovies: () -> Unit,
    navToTheatres: () -> Unit
) {
    AdminContentWrapper("DASHBOARD") {
        CustomButton(label = "MOVIES") { navToMovies() }
        CustomButton(label = "THEATRES") { navToTheatres() }
    }
}