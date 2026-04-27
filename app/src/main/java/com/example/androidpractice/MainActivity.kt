package com.example.androidpractice

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.androidpractice.ui.theme.AndroidPracticeTheme
import com.example.androidpractice.utils.NotificationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }
        NotificationHelper.createNotificationChannel(this)
        super.onCreate(savedInstanceState)
        setContent {
            AndroidPracticeTheme {
                AppRoot()
            }
        }
    }
}