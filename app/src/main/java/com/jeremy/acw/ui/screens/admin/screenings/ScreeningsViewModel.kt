package com.jeremy.acw.ui.screens.admin.screenings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.repo.ScreeningRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ScreeningsViewModel @Inject constructor(
    private val screeningsRepo: ScreeningRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val theatreId = savedStateHandle.get<String>("theatreId")!!
    val hallId = savedStateHandle.get<String>("hallId")!!

    val hallName = savedStateHandle.get<String>("hallName")!!
    val hallSize = savedStateHandle.get<String>("hallSize")!!

    private val _screenings = MutableStateFlow<List<Screening>>(emptyList())
    val screenings = _screenings.asStateFlow()

    private val _filteredScreenings = MutableStateFlow<List<Screening>>(emptyList())
    val filteredScreenings = _filteredScreenings.asStateFlow()

    val currentDate = LocalDate.now()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchScreenings()
    }

    fun fetchScreenings() {
        _isLoading.value = true
        viewModelScope.launch {
            safeApiCall {
                val data = screeningsRepo.fetchAllScreenings(theatreId, hallId)
                _screenings.value = data
                filterScreenings(currentDate)
                _isLoading.value = false
            }
        }
    }

    fun filterScreenings(date: LocalDate) {
        val filteredDate = screenings.value.filter {
            it.startTime?.toDate()?.toInstant()
                ?.atZone(ZoneId.systemDefault())
                ?.toLocalDate() == date
        }
        _filteredScreenings.value = filteredDate.sortedBy { it.startTime }
    }
}