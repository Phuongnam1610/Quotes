package com.example.quotes.Model

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.type.DateTime
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
@Parcelize
data class Post(
    var id: String?,
    var content: String?,
    var author: String?,
    var userid: String?,
    val categoryid: String?,
    var createdat: Timestamp?,
    val updateat: String?,
    var image: String?,
    var liked:Boolean?,
    var likecount:Int?,
): Parcelable {
    constructor() : this(null,null,null,null,null,null,null,null,null,null)
    fun getImageUrl(): String {
        return image.takeIf { it != "null" && it != null } ?: "https://firebasestorage.googleapis.com/v0/b/quotes-b9c8e.appspot.com/o/peakpx.jpg?alt=media&token=69d365c1-bd1f-4e88-a214-cd037c9bfc3a"
    }
}
