package com.jeremy.acw.ui.screens.login

import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.requests.LoginReq
import com.jeremy.acw.data.repo.UserRepo
import com.jeremy.acw.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repo: UserRepo
): BaseViewModel() {
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    fun login(loginReq: LoginReq) {
        if(!validateLogin(loginReq)) return
        viewModelScope.launch {
            safeApiCall {
                repo.login(loginReq).let {
                    _finish.emit(Unit)
                }
            }
        }
    }
}