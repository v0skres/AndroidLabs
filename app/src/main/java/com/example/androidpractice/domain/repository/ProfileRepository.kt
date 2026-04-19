package com.example.androidpractice.domain.repository

import com.example.androidpractice.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun saveProfile(profile: Profile)
    suspend fun clearProfile()
    fun getProfileFlow(): Flow<Profile>
}