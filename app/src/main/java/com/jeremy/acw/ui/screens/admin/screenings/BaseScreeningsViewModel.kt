package com.jeremy.acw.ui.screens.admin.screenings

import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseScreeningsViewModel: BaseViewModel() {
    protected val _screenings = MutableStateFlow<List<Screening>>(emptyList())
    val screenings = _screenings.asStateFlow()
}