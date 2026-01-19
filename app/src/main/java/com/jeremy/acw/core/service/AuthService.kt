package com.jeremy.acw.core.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.jeremy.acw.data.model.user.FirebaseData
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val _user = MutableStateFlow<FirebaseData?>(null)
    val user = _user.asStateFlow()

    init {
        fetchLoggedInUser()
    }

    private fun fetchLoggedInUser() {
        _user.value = firebaseAuth.currentUser?.let {
            FirebaseData(it.uid)
        }
    }

    private fun updateUser(firebaseUser: FirebaseUser) {
        firebaseUser.let { user ->
            _user.update {
                FirebaseData(
                    uid = user.uid,
                    email = user.email.toString(),
                )
            }
        }
    }

    fun getCurrentUser(): FirebaseData? {
        return user.value
    }

    suspend fun register(email: String, password: String): String {
        val result =
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            } catch (e: FirebaseAuthUserCollisionException) {
                throw IllegalStateException("Email in-use")
            } catch (e: Exception) {
                throw e
            }

        return result.user?.uid ?: throw IllegalStateException("User creation failed")
    }

    suspend fun login(email: String, password: String): FirebaseUser {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        result.user?.let { updateUser(it) }

        return result.user ?: throw java.lang.IllegalStateException("Login failed")
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}