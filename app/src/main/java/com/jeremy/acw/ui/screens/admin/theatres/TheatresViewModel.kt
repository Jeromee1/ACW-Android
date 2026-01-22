package com.jeremy.acw.ui.screens.admin.theatres

import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.cinema.Theatre
import com.jeremy.acw.data.repo.TheatreRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TheatresViewModel @Inject constructor(
    private val theatreRepo: TheatreRepo
): BaseViewModel() {
    private val _theatres = MutableStateFlow<List<Theatre>>(emptyList())
    private val theatres = _theatres.asStateFlow()

    private val _sortedTheatres = MutableStateFlow<List<Theatre>>(emptyList())
    val sortedTheatres = _sortedTheatres.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchTheatres()
    }

    fun fetchTheatres() {
        _isLoading.value = true
        viewModelScope.launch {
            safeApiCall {
                val data = theatreRepo.fetchAllTheatres()
                _theatres.value = data
                _sortedTheatres.value = theatres.value
                _isLoading.value = false
            }
        }
    }

    fun addTheatre(theatre: String) {
        viewModelScope.launch {
            safeApiCall {
                val res = validateTheatre(theatre)
                if(res) {
                    theatreRepo.createTheatre(theatre)
                    fetchTheatres()
                }
            }
        }
    }

    fun deleteTheatre(id: String) {
        viewModelScope.launch {
            safeApiCall {
                theatreRepo.deleteTheatre(id)
                fetchTheatres()
            }
        }
    }

    fun search(search: String) {
        _sortedTheatres.value = theatres.value.filter {
            it.id.startsWith(search, ignoreCase = true)
        }
    }
}