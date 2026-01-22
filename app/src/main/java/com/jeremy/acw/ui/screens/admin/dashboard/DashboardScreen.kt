package com.jeremy.acw.ui.screens.admin.dashboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeremy.acw.ui.components.AdminContentWrapper
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

    Dashboard() {
//        navController.navigate(Screen.Theatres)
    }
}

@Composable
fun Dashboard(
    navToTheatres: () -> Unit
) {
    AdminContentWrapper("DASHBOARD") {
        Button(
            onClick = { navToTheatres() },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) { Text("THEATRES", modifier = Modifier.padding(8.dp)) }
    }
}