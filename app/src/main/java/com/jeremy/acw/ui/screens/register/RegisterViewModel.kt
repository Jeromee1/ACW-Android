package com.jeremy.acw.ui.screens.register

import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.forms.RegisterForm
import com.jeremy.acw.data.repo.UserRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val repo: UserRepo
): BaseViewModel() {
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    fun register(userReq: RegisterForm) {
        if (!validateRegisterFields(userReq)) return
        viewModelScope.launch {
            val success = safeApiCall {
                repo.register(userReq)
            }
            if (success != null) {
                _toast.emit("Registration Successful")
                _finish.emit(Unit)
            }
        }
    }
}