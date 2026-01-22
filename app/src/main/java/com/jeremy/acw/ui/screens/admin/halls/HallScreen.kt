package com.jeremy.acw.ui.screens.admin.halls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.hall.HallItem

@Composable
fun HallScreen(
    navController: NavController,
    viewModel: HallViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val halls by viewModel.halls.collectAsStateWithLifecycle()

    Hall(isLoading, halls) {
//        navController.navigate(Screen.Screenings)
    }
}

@Composable
fun Hall(
    isLoading: Boolean,
    halls: List<Hall>,
    onClicked: (String) -> Unit
) {
    AdminContentWrapper("HALLS") {
        Text(
            if(halls.isNotEmpty()) halls.first().theatreId else "",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        if(!isLoading) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(halls) {
                    HallItem(it) { onClicked(it.id) }
                }
            }
        } else {
            LoadingSpinner()
        }
    }
}