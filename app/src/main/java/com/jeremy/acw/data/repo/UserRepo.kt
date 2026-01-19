package com.jeremy.acw.data.repo

import com.jeremy.acw.data.model.user.forms.RegisterForm
import com.jeremy.acw.data.model.user.UserData
import com.jeremy.acw.data.model.user.forms.EditProfileForm
import com.jeremy.acw.data.model.user.requests.LoginReq

interface UserRepo {
    suspend fun register(user: RegisterForm)
    suspend fun login(req: LoginReq): UserData
    suspend fun fetchProfile(uid: String): UserData
    suspend fun updateProfile(uid: String, form: EditProfileForm)
}