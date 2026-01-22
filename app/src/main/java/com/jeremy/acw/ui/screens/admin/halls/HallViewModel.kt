package com.jeremy.acw.ui.screens.admin.halls

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.repo.HallRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HallViewModel @Inject constructor(
    private val hallRepo: HallRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val theatreId = savedStateHandle.get<String>("theatreId")!!

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _halls = MutableStateFlow<List<Hall>>(emptyList())
    val halls = _halls.asStateFlow()

    init {
        fetchHalls()
    }

    fun fetchHalls() {
        _isLoading.value = true
        viewModelScope.launch {
            safeApiCall {
                val data = hallRepo.fetchAllHalls(theatreId)
                _halls.value = data
                _isLoading.value = false
            }
        }
    }
}