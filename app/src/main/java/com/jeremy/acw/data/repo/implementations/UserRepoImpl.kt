package com.jeremy.acw.data.repo.implementations

import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.core.service.AuthService
import com.jeremy.acw.data.model.user.UserData
import com.jeremy.acw.data.model.forms.EditProfileForm
import com.jeremy.acw.data.model.forms.RegisterForm
import com.jeremy.acw.data.model.requests.LoginReq
import com.jeremy.acw.data.repo.UserRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class UserRepoImpl @Inject constructor(
    private val authService: AuthService,
    firestore: FirebaseFirestore
) : UserRepo {
    private val dbRef = firestore.collection("users")

    override suspend fun register(user: RegisterForm) {
        val uid = authService.register(user.email, user.password)
        val userData = UserData(
                uid = uid,
                fullname = user.fullname,
                email = user.email,
            )

        dbRef.document(uid).set(userData.toMap()).await()
    }

    override suspend fun login(req: LoginReq): UserData {
        return authService.login(req.email, req.password).let { user ->
            dbRef.document(user.uid)
                .get()
                .await()
                .toObject(UserData::class.java)
                ?: throw IllegalStateException("User doesn't exist")
        }
    }

    override suspend fun fetchProfile(uid: String): UserData {
        return dbRef.document(uid)
            .get()
            .await()
            .toObject(UserData::class.java)
            ?: throw java.lang.IllegalStateException("User doesn't exist")
    }

    override suspend fun updateProfile(uid: String, form: EditProfileForm) {
        dbRef.document(uid)
            .update("fullname", form.fullname)
            .await()
    }
}