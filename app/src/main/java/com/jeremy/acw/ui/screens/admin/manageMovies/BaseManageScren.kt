package com.jeremy.acw.ui.screens.admin.manageMovies

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.google.firebase.Timestamp
import com.jeremy.acw.data.enums.cinema.AgeRating
import com.jeremy.acw.data.enums.cinema.Genre
import com.jeremy.acw.data.enums.cinema.Languages
import com.jeremy.acw.data.enums.cinema.Subtitles
import com.jeremy.acw.data.model.forms.MovieForm
import com.jeremy.acw.data.model.ui.FieldData
import com.jeremy.acw.ui.components.core.SkeletonUI
import com.jeremy.acw.ui.components.inputs.CustomButton
import com.jeremy.acw.ui.components.inputs.CustomChipSelect
import com.jeremy.acw.ui.components.inputs.CustomDropdown
import com.jeremy.acw.ui.components.inputs.CustomTextField
import com.jeremy.acw.ui.theme.Secondary
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BaseManageScreen(
    btnLabel: String,
    fetchedMovie: MovieForm = MovieForm(),
    submit: (MovieForm) -> Unit
) {
    var movieData by remember { mutableStateOf(fetchedMovie) }
    var showDatePicker by remember { mutableStateOf(false) }

    BaseManage(
        btnLabel,
        movieData,
        { movieData = it },
        { showDatePicker = true },
        { submit(movieData) }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = movieData.releaseDate.toDate().time
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val normalizedMillis = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                            val firebaseTimestamp = Timestamp(Date(normalizedMillis))
                            movieData = movieData.copy(releaseDate = firebaseTimestamp)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BaseManage(
    btnLabel: String,
    data: MovieForm,
    onDataChanged: (MovieForm) -> Unit,
    onDateClicked: () -> Unit,
    submit: () -> Unit,
) {
    val convertedTime = data.duration / 60
    val hours = convertedTime / 60
    val minutes = convertedTime % 60

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = data.imgUrl,
            contentDescription = null,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(0.6f)
                .aspectRatio(2f/ 3f)
                .background(Secondary),
            contentScale = ContentScale.FillWidth,
            loading = { SkeletonUI() }
        )
        CustomTextField(
            FieldData("MovieUrl", data.imgUrl) {
                onDataChanged(data.copy(imgUrl = it))
            }
        )
        CustomTextField(
            FieldData("Title", data.title) {
                onDataChanged(data.copy(title = it))
            }
        )
        CustomTextField(
            FieldData("Description", data.description) {
                onDataChanged(data.copy(description = it))
            }
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Hours")
                            CustomDropdown(
                                items = (0..23).toList(),
                                selectedItem = hours.toString(),
                                itemLabel = { it.toString() }
                            ) {
                                val newDuration = ((it.toLong() * 60) + minutes) * 60
                                onDataChanged(data.copy(duration = newDuration))
                            }
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Minutes")
                            CustomDropdown(
                                items = (0..59).toList(),
                                selectedItem = minutes.toString(),
                                itemLabel = { it.toString() },
                            ) {
                                val newDuration = ((hours * 60) + it.toLong()) * 60
                                onDataChanged(data.copy(duration = newDuration))
                            }
                        }
                    }
                }
            }
        }
        CustomChipSelect(
            "Genre",
            selectedItems = data.genre,
            items = Genre.entries.map { it.value },
            label = { it }
        ) {
            onDataChanged(
                data.copy(
                    genre = if (it in data.genre) data.genre - it
                    else data.genre + it
                )
            )
        }
        CustomChipSelect(
            "Language",
            selectedItems = data.language,
            items = Languages.entries.map { it.value },
            label = { it }
        ) {
            onDataChanged(
                data.copy(
                    language = if (it in data.language) data.language - it
                    else data.language + it
                )
            )
        }
        CustomChipSelect(
            "Subtitle",
            selectedItems = data.subtitles,
            items = Subtitles.entries.map { it.value },
            label = { it }
        ) {
            onDataChanged(
                data.copy(
                    subtitles = if (it in data.subtitles) data.subtitles - it
                    else data.subtitles + it
                )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Age Rating:",
                fontSize = 18.sp
            )
            CustomDropdown(
                AgeRating.entries.map { it.value },
                data.ageRating,
                itemLabel = { it },
            ) {
                onDataChanged(data.copy(ageRating = it))
            }
        }
        CustomButton("Select Release Date") { onDateClicked() }
        Spacer(Modifier.height(20.dp))
        CustomButton(btnLabel) { submit() }
    }
}