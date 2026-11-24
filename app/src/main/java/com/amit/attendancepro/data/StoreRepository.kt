package com.amit.attendancepro.data

import com.amit.attendancepro.domain.model.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase
) {

    suspend fun createStore(name: String, address: String): Result<Store> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("Not logged in"))
            val storeId = db.getReference("attendancepro/stores").push().key
                ?: return Result.failure(Exception("Failed to generate ID"))
            val store = Store(
                id = storeId,
                name = name,
                address = address,
                adminUid = user.uid,
                adminEmail = user.email ?: "",
                qrToken = "AMITCLIP_STORE:$storeId:${UUID.randomUUID()}",
                createdAt = System.currentTimeMillis()
            )
            db.getReference("attendancepro/stores").child(storeId).setValue(store).await()
            db.getReference("attendancepro/users").child(user.uid).child("storeId").setValue(storeId).await()
            Result.success(store)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyStore(): Result<Store?> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("Not logged in"))
            val snap = db.getReference("attendancepro/users").child(user.uid).child("storeId").get().await()
            val storeId = snap.getValue(String::class.java) ?: return Result.success(null)
            val storeSnap = db.getReference("attendancepro/stores").child(storeId).get().await()
            Result.success(storeSnap.getValue(Store::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStoreById(storeId: String): Result<Store?> {
        return try {
            val snap = db.getReference("attendancepro/stores").child(storeId).get().await()
            Result.success(snap.getValue(Store::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllStores(): Result<List<Store>> {
        return try {
            val snap = db.getReference("attendancepro/stores").get().await()
            val stores = mutableListOf<Store>()
            snap.children.forEach { child ->
                child.getValue(Store::class.java)?.let { stores.add(it) }
            }
            Result.success(stores.sortedByDescending { it.createdAt })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
