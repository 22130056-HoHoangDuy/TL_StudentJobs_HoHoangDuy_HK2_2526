package com.studentjobs.app.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthService {

    private val auth = FirebaseAuth.getInstance()

    suspend fun register(email: String, password: String)
            = auth.createUserWithEmailAndPassword(email, password).await()

    fun getCurrentUser() = auth.currentUser
    suspend fun login(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await()
    //logout
    fun logout() {
        auth.signOut()
    }
}