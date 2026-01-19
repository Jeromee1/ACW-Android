package com.jeremy.acw.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
}