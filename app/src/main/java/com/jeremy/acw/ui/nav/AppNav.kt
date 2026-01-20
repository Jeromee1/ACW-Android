package com.jeremy.acw.ui.nav

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeremy.acw.ui.components.core.CustomTopBar
import com.jeremy.acw.ui.screens.home.HomeScreen
import com.jeremy.acw.ui.screens.login.LoginScreen
import com.jeremy.acw.ui.screens.register.RegisterScreen
import com.jeremy.acw.ui.theme.Background
import com.jeremy.acw.ui.theme.KindaWhite

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val dest = navBackStackEntry?.destination

    val showTopBar = when {
        dest == null -> false
        dest.hasRoute<Screen.Splash>() -> false
        else -> true
    }

    val showBackBtn = when {
        dest == null -> false
        dest.hasRoute<Screen.Login>() ||
        dest.hasRoute<Screen.Register>() ||
        dest.hasRoute<Screen.Home>() -> false
        else -> true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentColor = KindaWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(if (!showTopBar) innerPadding else PaddingValues())
        ) {
            if (showTopBar) {
                CustomTopBar(
                    navController = navController,
                    showBackBtn = showBackBtn
                )
            }
            Nav(navController)
        }
    }
}

@Composable
fun Nav(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
//        composable<Screen.Splash> { SplashScreen(navController) }
        composable<Screen.Login> { LoginScreen(navController) }
        composable<Screen.Register> { RegisterScreen(navController) }
        composable<Screen.Home> { HomeScreen(navController) }
    }
}
