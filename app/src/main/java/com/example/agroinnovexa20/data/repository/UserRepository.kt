package com.example.agroinnovexa20.data.repository

import com.example.agroinnovexa20.data.model.profile.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun saveProfile(profile: UserProfile): Result<Unit> {
        return try {
            val uid = auth.currentUser!!.uid
            db.collection("users").document(uid).set(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<UserProfile> {
        return try {
            val uid = auth.currentUser!!.uid
            val doc = db.collection("users").document(uid).get().await()
            val profile = doc.toObject(UserProfile::class.java) ?: UserProfile()
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}