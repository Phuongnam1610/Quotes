package com.example.quotes.Model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.Date
@Parcelize
data class Comment(
    var id: String? = null,
    var postid: String? = null,
    val content: String? = null,
    var userid: String? = null,
    var userid2:String?=null,
    var createdat: Timestamp? = null,

) : Parcelable {
    constructor():this(null,null,null,null,null)
}