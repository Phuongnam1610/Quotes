package com.example.quotes.fragments.notif

import com.example.quotes.Model.Notification
import com.example.quotes.Model.Post
import com.example.quotes.base.IMVPView

interface INotifView :IMVPView{
    fun addNotif(notif:MutableList<Notification>)
    fun showQuotesFragment(post: Post)
}