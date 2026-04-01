package com.example.androidpractice.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

data class FilterSettings(
    val owner: String = DEFAULT_OWNER,
    val minStars: Int = 0,
    val language: String = ""
) {
    fun isDefault(): Boolean = owner == DEFAULT_OWNER && minStars == 0 && language.isBlank()

    companion object {
        const val DEFAULT_OWNER = "google"
    }
}

class SettingsDataStore(private val context: Context) {

    private val KEY_OWNER = stringPreferencesKey("owner")
    private val KEY_MIN_STARS = intPreferencesKey("min_stars")
    private val KEY_LANGUAGE = stringPreferencesKey("language")

    val settingsFlow: Flow<FilterSettings> =
        context.dataStore.data.map { prefs ->
            FilterSettings(
                owner = prefs[KEY_OWNER] ?: FilterSettings.DEFAULT_OWNER,
                minStars = prefs[KEY_MIN_STARS] ?: 0,
                language = prefs[KEY_LANGUAGE] ?: ""
            )
        }

    suspend fun save(settings: FilterSettings) {
        context.dataStore.edit { prefs ->
            prefs[KEY_OWNER] = settings.owner
            prefs[KEY_MIN_STARS] = settings.minStars
            prefs[KEY_LANGUAGE] = settings.language
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}

