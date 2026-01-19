package com.jeremy.acw.data.model.user

import com.jeremy.acw.data.enums.Roles

data class UserData(
    val uid: String = "",
    val fullname: String = "",
    val email: String = "",
    val role: String = Roles.USER.value
) {
    fun toMap(): Map<String, Any> = mutableMapOf(
        "uid" to uid,
        "fullname" to fullname,
        "email" to email,
        "role" to role
    )
}