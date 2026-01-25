package com.jeremy.acw.ui.screens.admin.screenings

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

@HiltViewModel
class ScreeningsViewModel @Inject constructor(
    private val screeningsRepo: ScreeningRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val theatreId = savedStateHandle.get<String>("theatreId")!!
    val hallId = savedStateHandle.get<String>("hallId")!!

    val hallName = savedStateHandle.get<String>("hallName")

    private val _screenings = MutableStateFlow<List<Screening>>(emptyList())
    val screenings = _screenings.asStateFlow()

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
                _isLoading.value = false
            }
        }
    }
}