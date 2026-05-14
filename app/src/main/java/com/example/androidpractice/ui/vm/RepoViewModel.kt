package com.example.androidpractice.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.data.repository.RepoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepoViewModel(
    private val repository: RepoRepository = RepoRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<RepoUIState>(RepoUIState.Loading)
    val state: StateFlow<RepoUIState> = _state

    fun load(owner: String) {
        viewModelScope.launch {
            _state.value = RepoUIState.Loading
            try {
                val repos = repository.loadRepos(owner)
                _state.value = RepoUIState.Content(repos)
            } catch (e: Exception) {
                _state.value = RepoUIState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getById(id: Long) =
        (state.value as? RepoUIState.Content)
            ?.items
            ?.firstOrNull { it.id == id }
}