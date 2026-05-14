package com.example.androidpractice

import android.app.Application

class AndroidPracticeApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}