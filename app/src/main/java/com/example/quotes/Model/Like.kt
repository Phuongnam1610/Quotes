package com.example.quotes.Model

import com.google.type.DateTime

data class Like(val id: String, var userid: String, var postid:String, var createdAt: Any?)
{
    constructor(): this("","","",null)
}
