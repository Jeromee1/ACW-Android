package com.jeremy.acw.data.model.ui

data class FieldData(
    val label: String,
    val value: String,
    val isPassword: Boolean = false,
    val onValueChange: (String) -> Unit
)