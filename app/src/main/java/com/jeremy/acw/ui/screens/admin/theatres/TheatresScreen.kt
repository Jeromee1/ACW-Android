package com.jeremy.acw.ui.screens.admin.theatres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.cinema.Theatre
import com.jeremy.acw.data.model.ui.FieldData
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.core.CustomButton
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomTextField
import com.jeremy.acw.ui.components.theatre.TheatreItem
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.KindaWhite

@Composable
fun TheatresScreen(
    navController: NavController,
    viewModel: TheatresViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val sortedTheatres by viewModel.sortedTheatres.collectAsStateWithLifecycle()
    var theatre by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }

    Theatres(
        isLoading,
        sortedTheatres,
        theatre,
        { theatre = it },
        { viewModel.addTheatre(theatre); theatre = "" },
        { viewModel.deleteTheatre(it) },
        search,
        { search = it; viewModel.search(search) }
    ) { navController.navigate(Screen.Hall(it)) }
}

@Composable
fun Theatres(
    isLoading: Boolean,
    theatres: List<Theatre>,
    theatre: String,
    onTheatreChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onDeleteClicked: (String) -> Unit,
    search: String,
    onSearchChanged: (String) -> Unit,
    onClicked: (String) -> Unit
) {
    AdminContentWrapper("THEATRES") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(FieldData("Theatre", theatre) { onTheatreChanged(it) })
            CustomButton("Add") { onAddClicked() }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = KindaWhite,
            modifier = Modifier.padding(0.dp, 12.dp)
        )
        CustomTextField(FieldData("Search", search) { onSearchChanged(it) })
        if(!isLoading) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(theatres) {
                    TheatreItem(
                        it,
                        0,
                        { onClicked(it.id) }
                    ) { onDeleteClicked(it.id) }
                }
            }
        } else {
            LoadingSpinner()
        }
    }
}