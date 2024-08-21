package com.example.quotes.repository

import com.example.quotes.Model.Notification
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotificationRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("Users")
    val userid = FirebaseAuth.getInstance().currentUser!!.uid

   suspend fun addNotification(userid2: String, notification: Notification) {
        notification.time = Timestamp.now()
        val notifData = hashMapOf(
            "type" to notification.type,
            "userid2" to notification.userid2,
            "time" to notification.time,
            "postid" to notification.postid
        )
       userCollection.document(userid2).collection("Notifications").add(notifData).await()
    }

    fun getAllNotifByUserID(userID: String, callback: (MutableList<Notification>) -> Unit) {
        val notifCollection = userCollection.document(userID)
            .collection("Notifications")

        notifCollection.get()
            .addOnSuccessListener { result ->
                val notifications =
                    result.documents.mapNotNull { it.toObject(Notification::class.java) }
                callback(notifications.toMutableList())
            }
            .addOnFailureListener {
                callback(mutableListOf())
            }
    }


    fun addListenerNotification(listener: (MutableList<Notification>) -> Unit) {
        var init = true
        val notifCollection = userCollection.document(userid).collection("Notifications")
        notifCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (!init) {
                if (snapshot != null) {
                    val lnotif = mutableListOf<Notification>()
                    for (documentChange in snapshot.documentChanges) {
                        val notification =
                            documentChange.document.toObject(Notification::class.java)
                        lnotif.add(notification)
                    }
                    lnotif.sortByDescending { it.time }
                    listener(lnotif)
                }
            } else {
                init = false
            }
        }
    }


}