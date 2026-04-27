package com.example.androidpractice.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("profile_name") ?: "студент"
        NotificationHelper.createNotificationChannel(context)
        NotificationHelper.showReminderNotification(context, name)
    }
}