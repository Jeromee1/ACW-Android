package com.jeremy.acw.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeremy.acw.ui.components.core.CustomMenu
import com.jeremy.acw.ui.components.core.CustomTopBar
import com.jeremy.acw.ui.screens.admin.dashboard.DashboardScreen
import com.jeremy.acw.ui.screens.admin.halls.HallScreen
import com.jeremy.acw.ui.screens.admin.manageMovies.add.AddMovieScreen
import com.jeremy.acw.ui.screens.admin.manageMovies.edit.EditMovieScreen
import com.jeremy.acw.ui.screens.admin.manageSeats.ManageSeatsScreen
import com.jeremy.acw.ui.screens.admin.overview.OverviewScreen
import com.jeremy.acw.ui.screens.admin.screenings.ScreeningsAddScreen
import com.jeremy.acw.ui.screens.admin.screenings.ScreeningsScreen
import com.jeremy.acw.ui.screens.admin.theatres.TheatresScreen
import com.jeremy.acw.ui.screens.details.DetailsScreen
import com.jeremy.acw.ui.screens.home.HomeScreen
import com.jeremy.acw.ui.screens.login.LoginScreen
import com.jeremy.acw.ui.screens.profile.ProfileScreen
import com.jeremy.acw.ui.screens.register.RegisterScreen
import com.jeremy.acw.ui.theme.Background
import com.jeremy.acw.ui.theme.KindaWhite

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNav() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val dest = navBackStackEntry?.destination
    var menuToggle by remember { mutableStateOf(false) }

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
                    showBackBtn = showBackBtn,
                    showMenu = dest!!.hasRoute<Screen.Home>()
                ) { menuToggle = true }
            }
            Nav(navController)
        }
        CustomMenu(
            menuToggle,
            { menuToggle = false },
            { navController.navigate(Screen.Profile) },
            { /*navController.navigate(Screen.Bookings)*/ },
            { navController.navigate(Screen.Login) },
            { /*navController.navigate(Screen.About)*/ },
            { navController.navigate(Screen.Dashboard) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Nav(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
//        composable<Screen.Splash> { SplashScreen(navController) }
        composable<Screen.Login> { LoginScreen(navController) }
        composable<Screen.Register> { RegisterScreen(navController) }
        composable<Screen.Home> { HomeScreen(navController) }
        composable<Screen.Details> {
            val isLoggedIn = it.savedStateHandle.get<Boolean>("isLoggedIn") ?: false
            DetailsScreen(isLoggedIn, navController)
        }
        composable<Screen.Profile> { ProfileScreen(navController) }
        composable<Screen.Dashboard> { DashboardScreen(navController) }
        composable<Screen.Theatres> { TheatresScreen(navController) }
        composable<Screen.Hall> { HallScreen(navController) }
        composable<Screen.Screenings> { ScreeningsScreen(navController) }
        composable<Screen.ScreeningsAdd> { ScreeningsAddScreen(navController) }
        composable<Screen.ManageSeats> { ManageSeatsScreen(navController) }
        composable<Screen.OverviewMovies> { OverviewScreen(navController) }
        composable<Screen.AddMovie> { AddMovieScreen(navController) }
        composable<Screen.EditMovie> { EditMovieScreen(navController) }
    }
}
