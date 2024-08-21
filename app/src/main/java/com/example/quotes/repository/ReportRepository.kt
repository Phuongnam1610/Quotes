package com.example.quotes.repository

import com.example.quotes.Model.Notification
import com.example.quotes.Model.Report
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReportRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun addReport(report: Report): Boolean {
       return try {
            report.createdAt = serverTimestamp()
            val userid = FirebaseAuth.getInstance().currentUser!!.uid
            report.userid = userid
            val reportData = hashMapOf(
                "userid" to report.userid,
                "createdat" to report.createdAt
            )
            firestore.collection("Posts").document(report.postid).collection("Reports")
                .add(reportData).await()
            if (report.userid != report.userID2) {
                val notif = Notification(2, report.userid, report.postid, null)
                NotificationRepository().addNotification(report.userID2, notif)
            }
            val updateData = hashMapOf(
                "report" to FieldValue.increment(1) // Tăng giá trị field "report" lên 1
            )
            firestore.collection("Users").document(report.userID2)
                .update(updateData as Map<String, Any>).await()
            true
        } catch (e: Exception) {
            false
        }
    }

//    suspend fun unreport(report: Report): Boolean {
//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//        report.userid = userId
//
//        return try {
//            val postRef = firestore.collection("Posts").document(report.postid)
//            val reportsRef = postRef.collection("Reports")
//            val snapshot = reportsRef.whereEqualTo("userid", userId).limit(1).get().await()
//            for (document in snapshot.documents) {
//                reportsRef.document(document.id).delete().await()
//            }
//            true
//        } catch (e: Exception) {
//            throw e
//            false
//        }
//    }


}