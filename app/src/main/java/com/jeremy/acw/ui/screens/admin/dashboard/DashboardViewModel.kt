package com.jeremy.acw.ui.screens.admin.dashboard

import androidx.lifecycle.ViewModel
import com.jeremy.acw.core.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val auth: AuthService
): ViewModel() {
    val perms = auth.isAdmin()
}