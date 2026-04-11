package com.example.androidpractice.ui.vm

import com.example.androidpractice.ui.model.RepoUI

sealed interface RepoUIState {
    data object Loading : RepoUIState
    data class Content(val items: List<RepoUI>) : RepoUIState
    data class Error(val message: String) : RepoUIState
}