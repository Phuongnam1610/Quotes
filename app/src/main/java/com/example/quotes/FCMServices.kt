package com.example.quotes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMServices : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Lấy dữ liệu từ thông báo
        val title = message.notification?.title
        val body = message.notification?.body

        // Hiển thị thông báo sử dụng Notification Compat
        showNotificationCompat(title, body)
    }

    private fun showNotificationCompat(title: String?, body: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Tạo Notification Channel (cần cho Android 8.0 và mới hơn)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Tạo Notification Compat
        val builder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.mipmap.icfb)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Hiển thị thông báo
        notificationManager.notify(0, builder.build())
    }
}