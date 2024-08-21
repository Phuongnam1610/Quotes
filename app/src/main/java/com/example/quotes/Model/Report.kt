package com.example.quotes.Model



data class Report(val id: String, var userid: String,var userID2:String, var postid:String, var createdAt: Any?)
{
    constructor(): this("","","","",null)
}
