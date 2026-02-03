package com.jeremy.acw.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.user.UserData
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomButton
import com.jeremy.acw.ui.nav.Screen

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.navigate(Screen.Home) {
                popUpTo(Screen.Profile) { inclusive = true }
            }
        }
    }

    if(isLoading) {
        LoadingSpinner()
    } else {
        if(user == null) navController.popBackStack()
        Profile(user!!) { viewModel.logOut() }
    }
}

@Composable
fun Profile(
    user: UserData,
    onClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                user.fullname,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                user.email,
                fontSize = 20.sp
            )
        }
        CustomButton(
            modifier = Modifier
                .padding(20.dp, 40.dp)
                .align(Alignment.BottomCenter),
            label = "Log Out"
        ) { onClicked() }
    }
}