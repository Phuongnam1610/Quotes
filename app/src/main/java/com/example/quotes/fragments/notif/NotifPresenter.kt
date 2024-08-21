package com.example.quotes.fragments.notif

import com.example.quotes.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth

class NotifPresenter(private val view: INotifView) {

    fun getAllNotif(){
        val uid=FirebaseAuth.getInstance().uid!!
        NotificationRepository().getAllNotifByUserID(uid){
            if(it.isNotEmpty()){
                it.sortByDescending { it.time }
                view.addNotif(it)
            }
        }
    }


}