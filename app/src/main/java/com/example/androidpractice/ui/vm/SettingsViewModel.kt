package com.example.androidpractice.ui.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.data.local.FilterSettings
import com.example.androidpractice.data.local.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val store: SettingsDataStore
) : ViewModel() {

    val settings: StateFlow<FilterSettings> =
        store.settingsFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FilterSettings()
        )

    fun save(newSettings: FilterSettings) {
        viewModelScope.launch { store.save(newSettings) }
    }

    fun clearAll() {
        viewModelScope.launch { store.clearAll() }
    }
}

class SettingsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val store = SettingsDataStore(context.applicationContext)
        @Suppress("UNCHECKED_CAST")
        return SettingsViewModel(store) as T
    }
}