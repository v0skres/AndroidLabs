package com.example.androidpractice.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.data.repository.FavoriteRepository
import com.example.androidpractice.ui.model.RepoUI
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repo: FavoriteRepository
) : ViewModel() {

    val favorites: StateFlow<List<RepoUI>> =
        repo.favoritesFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun toggleFavorite(item: RepoUI) {
        viewModelScope.launch { repo.toggle(item) }
    }
}

class FavoritesViewModelFactory(
    private val repo: FavoriteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return FavoritesViewModel(repo) as T
    }
}