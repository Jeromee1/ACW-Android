package com.jeremy.acw.data.model.forms

data class RegisterForm(
    val fullname: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = ""
)