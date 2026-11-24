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

    suspend fun signUp(name: String, email: String, password: String, role: UserRole): Result<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("No UID"))

            val finalRole = when {
                email.equals(SUPER_ADMIN_EMAIL, ignoreCase = true) -> UserRole.SUPER_ADMIN
                role == UserRole.SUPER_ADMIN -> UserRole.EMPLOYEE
                else -> role
            }

            val profile = UserProfile(
                uid = uid,
                name = name,
                email = email,
                role = finalRole.name,
                createdAt = System.currentTimeMillis()
            )
            db.getReference("attendancepro").child("users").child(uid).setValue(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchProfile(): Result<UserProfile> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("Not logged in"))
            val uid = user.uid
            val snap = db.getReference("attendancepro").child("users").child(uid).get().await()
            val profile = snap.getValue(UserProfile::class.java)
                ?: return Result.failure(Exception("Profile not found"))

            if (user.email.equals(SUPER_ADMIN_EMAIL, ignoreCase = true)
                && profile.role != UserRole.SUPER_ADMIN.name) {
                val updated = profile.copy(role = UserRole.SUPER_ADMIN.name)
                db.getReference("attendancepro").child("users").child(uid).setValue(updated).await()
                return Result.success(updated)
            }

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = auth.signOut()

    companion object {
        const val SUPER_ADMIN_EMAIL = "amitdaskishanganj@gmail.com"
    }
}
