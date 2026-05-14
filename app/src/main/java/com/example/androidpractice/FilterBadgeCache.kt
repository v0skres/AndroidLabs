package com.example.androidpractice.di

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterBadgeCache {
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge

    fun setShow(show: Boolean) { _showBadge.value = show }
}