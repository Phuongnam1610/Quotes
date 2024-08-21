package com.example.quotes.activities.home

import com.example.quotes.repository.NotificationRepository

class HomePresenter(val view:IHomeView) {

    fun addListenerNotif(){
        NotificationRepository().addListenerNotification(){
            view.displayNotification(it)
        }
    }
}