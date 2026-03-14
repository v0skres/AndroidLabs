package com.example.androidpractice.ui.vm

import androidx.lifecycle.ViewModel
import com.example.androidpractice.ui.mock.RepoMock
import com.example.androidpractice.ui.model.RepoUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReposViewModel : ViewModel() {
    private val _repos = MutableStateFlow(RepoMock.items)
    val repos: StateFlow<List<RepoUI>> = _repos

    fun getById(id: Long): RepoUI? = _repos.value.firstOrNull { it.id == id }
}