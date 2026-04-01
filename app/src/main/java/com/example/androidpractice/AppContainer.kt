package com.example.androidpractice

import android.content.Context
import com.example.androidpractice.data.db.AppDatabase
import com.example.androidpractice.data.repository.FavoritesRepository

class AppContainer(context: Context) {
    private val db: AppDatabase = AppDatabase.create(context)
    val favoritesRepository: FavoritesRepository = FavoritesRepository(db.favoritesDao())
}
