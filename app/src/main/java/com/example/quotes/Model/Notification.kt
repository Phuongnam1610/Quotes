package com.example.quotes.Model

import com.google.firebase.Timestamp

data class Notification(
    val type:Int?,
//    var userid:String?,
    val userid2:String?,
    val postid:String?,
    var time:Timestamp?
)
{
    constructor():this(null, null,null,null)
}
