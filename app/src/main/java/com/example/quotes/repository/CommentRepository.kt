package com.example.quotes.repository

import android.net.Uri
import android.util.Log
import android.view.View
import com.example.quotes.Model.Comment
import com.example.quotes.Model.Like
import com.example.quotes.Model.Notification
import com.example.quotes.Model.Post
import com.example.quotes.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class CommentRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val postCollection = firestore.collection("Posts")

    suspend fun addComment(comment: Comment): Boolean {
        return try {
            comment.createdat = Timestamp.now()

            val commentData = hashMapOf(
                "content" to comment.content,
                "userid" to comment.userid,
                "createdat" to comment.createdat
            )
            firestore.collection("Posts").document(comment.postid!!).collection("Comments")
                .add(commentData).await()
            if (comment.userid != comment.userid2) {
                val notif = Notification(1, comment.userid, comment.postid, null)
                CoroutineScope(Dispatchers.IO).launch {
                    NotificationRepository().addNotification(comment.userid2!!, notif)
                }
                Utils.sendNotification(comment.userid!!, comment.userid2!!, 1)
            }
            true
        } catch (e: Exception) {
            throw e
            false
        }
    }

    suspend fun deleteComment(comment: Comment): Boolean {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        comment.userid = userId
        return try {
            val postRef = firestore.collection("Posts").document(comment.postid!!)
            val likesRef = postRef.collection("Comments").document(comment.id!!).delete().await()
            true
        } catch (e: Exception) {
            throw e
            false
        }
    }

    fun addListenerNewListComment(
        postId: String, iChange: (MutableList<Comment>, Int) -> Unit,
    ) {
        try {
            val lcm = mutableListOf<Comment>()
            var init = true
            val commentCollection = postCollection.document(postId).collection("Comments")
            commentCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    for (documentChange in snapshot.documentChanges) {
                        val comment = documentChange.document.toObject(Comment::class.java)
                        comment.id = documentChange.document.id
                        comment.postid = postId
                        var mode = 0
                        when (documentChange.type) {
                            DocumentChange.Type.ADDED -> {
                                lcm.add(comment)
                                mode = 0
                            }

                            DocumentChange.Type.MODIFIED -> {
                                lcm.add(comment)
                                mode = 1
                            }

                            DocumentChange.Type.REMOVED -> {
                                lcm.add(comment)
                                mode = 2
                            }
                        }
                        if (!init) {
                            iChange(lcm, mode)
                            lcm.clear()
                        }

                    }
                    if (init) {
                        lcm.sortByDescending { it.createdat }
                        iChange(lcm, 0)
                        lcm.clear()
                    }

                }
                init = false

            }

        } catch (e: Exception) {

        }
    }

    suspend fun updateComment(comment: Comment):Boolean {
        return try {
            val updateData = hashMapOf("content" to comment.content)
            FirebaseFirestore.getInstance().collection("Posts").document(comment.postid!!)
                .collection("Comments").document(comment.id!!)
                .update(updateData as Map<String, Any>).await()
            true
        }catch (e:Exception){
            false
        }
    }


}