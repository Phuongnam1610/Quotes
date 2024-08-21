package com.example.quotes.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String? = null,
    var email: String? = null,
    var password: String? = null,
    val name: String? = null,
    var image: String? = null,
    val linkFB:String? =null,
    val token:String?=null
): Parcelable
{
    constructor() : this(null,null, null,null, null,null)
    fun getImageUrl(): String {
        return image.takeIf { it != "null" && it != null } ?: "https://firebasestorage.googleapis.com/v0/b/quotes-b9c8e.appspot.com/o/2534e19e6fa3ec6371ac572a91e891d9.jpg?alt=media&token=70897a1a-9c6c-40dc-8a25-fc4c84ea8d7d"
    }
}