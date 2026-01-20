package com.jeremy.acw.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
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
import com.jeremy.acw.data.model.forms.RegisterForm
import com.jeremy.acw.data.model.ui.FieldData
import com.jeremy.acw.ui.components.inputs.CustomTextField
import com.jeremy.acw.ui.theme.KindaWhite
import com.jeremy.acw.ui.theme.Primary

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.finish.collect {
            navController.popBackStack()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    Register(
        fullname,
        email,
        password,
        passwordConfirm,
        { fullname = it },
        { email = it },
        { password = it },
        { passwordConfirm = it },
        { navController.popBackStack() }
    ) { viewModel.register(RegisterForm(
        fullname,
        email,
        password,
        passwordConfirm
    )) }
}

@Composable
fun Register(
    fullname: String,
    email: String,
    password: String,
    passwordConfirm: String,
    onFullnameChange: (String) -> Unit,
    onChangeEmail: (String) -> Unit,
    onPassChange: (String) -> Unit,
    onPassConfirmChange: (String) -> Unit,
    onLogClicked: () -> Unit,
    onSubmit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
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
                    "Register",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                CustomTextField(
                    FieldData("Fullname", fullname) { onFullnameChange(it) }
                )
                CustomTextField(
                    FieldData("Email", email) { onChangeEmail(it) }
                )
                CustomTextField(
                    FieldData("Password", password, true) { onPassChange(it) }
                )
                CustomTextField(
                    FieldData("Confirm Password", passwordConfirm, true) { onPassConfirmChange(it) }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account?")
                    TextButton(
                        onClick = { onLogClicked() }
                    ) { Text("Login", color = Primary, fontSize = 16.sp) }
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
        ) { Text("Register", fontSize = 18.sp) }
    }
}