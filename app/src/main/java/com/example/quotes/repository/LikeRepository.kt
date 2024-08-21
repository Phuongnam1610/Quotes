package com.example.quotes.repository

import android.net.Uri
import android.util.Log
import android.view.View
import com.example.quotes.Model.Like
import com.example.quotes.Model.Notification
import com.example.quotes.Model.Post
import com.example.quotes.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue.serverTimestamp

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LikeRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun addlike(userid2: String, like: Like): Boolean {
        return try {
            like.createdAt = serverTimestamp()
            val userid = FirebaseAuth.getInstance().currentUser!!.uid
            like.userid = userid
            val likeData = hashMapOf(
                "userid" to like.userid,
                "createdat" to like.createdAt
            )

            firestore.collection("Posts").document(like.postid).collection("Likes")
                .add(likeData).await()
            if (like.userid != userid2) {
                val notif = Notification(0, like.userid, like.postid, null)
                NotificationRepository().addNotification(userid2, notif)
                Utils.sendNotification(userid, userid2, 0)
            }

            true
        } catch (e: Exception) {
            throw e
            false
        }
    }

    suspend fun unlike(like: Like): Boolean {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        like.userid = userId

        return try {
            val postRef = firestore.collection("Posts").document(like.postid)
            val likesRef = postRef.collection("Likes")
            val snapshot = likesRef.whereEqualTo("userid", userId).limit(1).get().await()
            for (document in snapshot.documents) {
                likesRef.document(document.id).delete().await()
            }
            true
        } catch (e: Exception) {
            throw e
            false
        }
    }


}