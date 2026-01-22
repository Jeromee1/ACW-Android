package com.jeremy.acw.ui.base

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeremy.acw.data.model.forms.RegisterForm
import com.jeremy.acw.data.model.requests.LoginReq
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel: ViewModel() {
    protected val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()
    suspend fun <T>safeApiCall(func: suspend () -> T?): T? {
        return try {
            val res = withContext(Dispatchers.IO) {
                func.invoke()
            }
            res
        } catch (e: Exception) {
            _toast.emit(e.message.toString())
            null
        }
    }

    fun validateLogin(form: LoginReq): Boolean {
        if (form.email.isBlank() || form.password.isBlank()) {
            emitToast("Fields cannot be blank")
            return false
        }
        return true
    }

    fun validateRegisterFields(form: RegisterForm): Boolean {
        form.apply {
            if(fullname.length < 7) return false
            if(!validateEmailFormat(email)) return false
            if(!validatePassword(password, passwordConfirm)) return false
        }
        return true
    }

    fun validateEmailFormat(email: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emitToast("Invalid email format")
            return false
        }
        return true
    }

    fun validatePassword(pass: String, pass2: String): Boolean {
        if (pass.length < 8) {
            emitToast("Password must be at least 8 characters")
            return false
        }

        if (pass != pass2) {
            emitToast("Passwords do not match")
            return false
        }
        return true
    }

    fun validateTheatre(theatre: String): Boolean {
        if(theatre.isBlank()) {
            emitToast("Theatre name cannot be blank")
            return false
        }
        return true
    }

    private fun emitToast(msg: String) {
        viewModelScope.launch {
            _toast.emit(msg)
        }
    }
}