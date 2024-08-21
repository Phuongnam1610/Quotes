package com.example.quotes.activities.home

import com.example.quotes.Model.Notification
import com.example.quotes.base.IMVPView

interface IHomeView:IMVPView {
    fun displayNotification(lnotif:MutableList<Notification>)
}