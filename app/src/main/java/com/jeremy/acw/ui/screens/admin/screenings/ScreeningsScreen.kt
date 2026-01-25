package com.jeremy.acw.ui.screens.admin.screenings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.theme.KindaWhite
import java.time.LocalDate

@Composable
fun ScreeningsScreen(
    navController: NavController,
    viewModel: ScreeningsViewModel = hiltViewModel()
) {
    val screenings = viewModel.screenings.collectAsStateWithLifecycle().value
    val hallName = viewModel.hallName
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Screenings(screenings, hallName ?: "")
}

@Composable
fun Screenings(
    screenings: List<Screening>,
    hallName: String
) {
    AdminContentWrapper("Hall $hallName - SCREENINGS") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ChevronLeft,
                null,
                tint = KindaWhite
            )
            Text(
                "22 January 2026",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
                )
            Icon(
                Icons.Default.ChevronRight,
                null,
                tint = KindaWhite
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

        }
    }
}