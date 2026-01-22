package com.jeremy.acw.ui.components.core

import androidx.lifecycle.ViewModel
import com.jeremy.acw.core.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class CustomMenuViewModel @Inject constructor(
    private val auth: AuthService
): ViewModel() {
    val user = auth.currentUser
}