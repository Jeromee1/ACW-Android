package com.jeremy.acw.ui.screens.profile

import androidx.lifecycle.viewModelScope
import com.jeremy.acw.core.service.AuthService
import com.jeremy.acw.data.model.user.UserData
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: AuthService
): BaseViewModel() {
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private val _user = MutableStateFlow<UserData?>(null)
    val user = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchUser()
    }

    fun fetchUser() {
        viewModelScope.launch {
            safeApiCall {
                val firebaseUser = auth.getCurrentUser()
                auth.fetchUserData(firebaseUser!!.uid)
                _user.value = auth.currentUser.value
                _isLoading.value = false
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            safeApiCall {
                auth.logout()
                _finish.emit(Unit)
            }
        }
    }
}