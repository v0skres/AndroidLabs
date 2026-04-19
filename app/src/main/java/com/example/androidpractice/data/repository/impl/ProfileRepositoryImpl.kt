package com.example.androidpractice.data.repository.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.androidpractice.domain.model.Profile
import com.example.androidpractice.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_profile")

class ProfileRepositoryImpl(private val context: Context) : ProfileRepository {

    private object Keys {
        val NAME = stringPreferencesKey("name")
        val AVATAR_URI = stringPreferencesKey("avatar_uri")
        val RESUME_URL = stringPreferencesKey("resume_url")
    }

    override suspend fun getProfile(): Profile {
        val prefs = context.dataStore.data.first()
        return Profile(
            name = prefs[Keys.NAME] ?: "",
            avatarUri = prefs[Keys.AVATAR_URI] ?: "",
            resumeUrl = prefs[Keys.RESUME_URL] ?: ""
        )
    }

    override suspend fun saveProfile(profile: Profile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NAME] = profile.name
            prefs[Keys.AVATAR_URI] = profile.avatarUri
            prefs[Keys.RESUME_URL] = profile.resumeUrl
        }
    }

    override suspend fun clearProfile() {
        context.dataStore.edit { it.clear() }
    }

    override fun getProfileFlow(): Flow<Profile> {
        return context.dataStore.data.map { prefs ->
            Profile(
                name = prefs[Keys.NAME] ?: "",
                avatarUri = prefs[Keys.AVATAR_URI] ?: "",
                resumeUrl = prefs[Keys.RESUME_URL] ?: ""
            )
        }
    }
}