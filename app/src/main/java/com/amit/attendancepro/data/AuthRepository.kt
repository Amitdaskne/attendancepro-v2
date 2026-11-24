package com.amit.attendancepro.data

import com.amit.attendancepro.domain.model.UserProfile
import com.amit.attendancepro.domain.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase
) {

    val currentUser get() = auth.currentUser
    val isLoggedIn get() = auth.currentUser != null

    suspend fun signUp(name: String, email: String, password: String, role: UserRole): Result<Unit> =
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("No UID"))

            val profile = UserProfile(
                uid = uid,
                name = name,
                email = email,
                role = role.name,
                createdAt = System.currentTimeMillis()
            )
            db.getReference("attendancepro").child("users").child(uid).setValue(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun login(email: String, password: String): Result<Unit> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun fetchProfile(): Result<UserProfile> =
        try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("Not logged in"))
            val snap = db.getReference("attendancepro").child("users").child(uid).get().await()
            val profile = snap.getValue(UserProfile::class.java)
                ?: return Result.failure(Exception("Profile not found"))
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }

    fun logout() = auth.signOut()
}
