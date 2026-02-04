package com.jeremy.acw.ui.screens.admin.theatres

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jeremy.acw.data.model.cinema.Theatre
import com.jeremy.acw.data.model.ui.FieldData
import com.jeremy.acw.ui.components.AdminContentWrapper
import com.jeremy.acw.ui.components.core.CustomBottomSheet
import com.jeremy.acw.ui.components.inputs.CustomButton
import com.jeremy.acw.ui.components.core.LoadingSpinner
import com.jeremy.acw.ui.components.inputs.CustomTextField
import com.jeremy.acw.ui.components.theatre.TheatreItem
import com.jeremy.acw.ui.nav.Screen
import com.jeremy.acw.ui.theme.KindaWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheatresScreen(
    navController: NavController,
    viewModel: TheatresViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val screeningsFetching by viewModel.screeningsFetching.collectAsStateWithLifecycle()
    val sortedTheatres by viewModel.sortedTheatres.collectAsStateWithLifecycle()
    var theatre by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toast.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Theatres(
        isLoading,
        sortedTheatres,
        theatre,
        { theatre = it },
        { viewModel.addTheatre(theatre); theatre = "" },
        { viewModel.fetchTheatreScreening(it); scope.launch { sheetState.show() } },
        search,
        { search = it; viewModel.search(search) }
    ) { navController.navigate(Screen.Hall(it)) }

    CustomBottomSheet(
        sheetState,
        { scope.launch { sheetState.hide() } }
    ) {
        val screeningsCount by viewModel.screeningsCount.collectAsStateWithLifecycle()
        val displayText = if(screeningsFetching) "Fetching..."
        else "Theatre Screenings: $screeningsCount"

        Text(
            displayText,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = KindaWhite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
    }
}

@Composable
fun Theatres(
    isLoading: Boolean,
    theatres: List<Theatre>,
    theatre: String,
    onTheatreChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
    onSelectedTheatre: (String) -> Unit,
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
            CustomButton(label = "Add") { onAddClicked() }
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
                        { onClicked(it.id) },
                        { onSelectedTheatre(it.id) }
                    )
                }
            }
        } else {
            LoadingSpinner()
        }
    }
}