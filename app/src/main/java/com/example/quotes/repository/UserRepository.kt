package com.example.quotes.repository

import android.util.Log
import com.example.quotes.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getUserById(userId: String): User? {
        return try {
            val snapshot = firestore.collection("Users")
                .document(userId)
                .get()
                .await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun banUpPost():Boolean{
        return try {
            val count=FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().uid!!)
                .get().await().getLong("report") ?: 0
            count>5
        }
        catch (e:Exception){
            e.printStackTrace()
            false
        }
    }



    suspend fun deleteToken(): Boolean {
        return try {
            FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().uid!!)
                .update("token", FieldValue.delete())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}