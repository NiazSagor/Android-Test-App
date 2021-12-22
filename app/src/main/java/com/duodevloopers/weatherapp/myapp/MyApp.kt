package com.duodevloopers.weatherapp.myapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build


class MyApp : Application() {

    companion object {
        val appId: String = "e384f9ac095b2109c751d95296f8ea76"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Weather Updates Notification Channel"
            val description = "Weather Notification"
            val channel = NotificationChannel(
                "push-notification-channel-id",
                name,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}