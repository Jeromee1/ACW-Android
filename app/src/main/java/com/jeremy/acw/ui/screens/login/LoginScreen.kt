package com.jeremy.acw.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeremy.acw.data.model.requests.LoginReq
import com.jeremy.acw.data.model.ui.FieldData
import com.jeremy.acw.ui.components.inputs.CustomTextField
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Primary

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.navigate(Screen.Home) {
                popUpTo(Screen.Login) { inclusive = true }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    Login(
        email,
        password,
        { email = it },
        { password = it },
        { navController.navigate(Screen.Register) }
    ) { viewModel.login(LoginReq(email, password)) }
}

@Composable
fun Login(
    email: String,
    password: String,
    onChangeEmail: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onRegClicked: () -> Unit,
    onSubmit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Icon(
            Icons.Outlined.Movie,
            "",
            tint = KindaWhite,
            modifier = Modifier
                .fillMaxSize(0.3f)
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
            )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Login",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                CustomTextField(
                    FieldData("Email", email) { onChangeEmail(it) }
                )
                CustomTextField(
                    FieldData("Password", password, true) { onPassChange(it) }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Don't have an account?")
                    TextButton(
                        onClick = { onRegClicked() }
                    ) { Text("Register", color = Primary, fontSize = 16.sp) }
                }
            }
        }
        FloatingActionButton(
            onClick = { onSubmit() },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            containerColor = Primary,
            contentColor = KindaWhite
        ) { Text("Login", fontSize = 18.sp) }
    }
}