package com.jeremy.acw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.jeremy.acw.ui.nav.AppNav
import com.jeremy.acw.ui.theme.ACWTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ACWTheme {
                ComposeApp()
            }
        }
    }
}

@Composable
fun ComposeApp() {
    AppNav()
}