package com.rickex.notivac.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        currentApplication = this
        createNotificationChannel()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Vaxinate Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    companion object{
        const val CHANNEL_ID = "VaxinateGeneralNotiChannel"

        private var currentApplication: MyApp? = null
        fun getInstance(): MyApp? {
            return currentApplication
        }
    }
}