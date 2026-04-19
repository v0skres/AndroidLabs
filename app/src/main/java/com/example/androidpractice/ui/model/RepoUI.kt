package com.example.androidpractice.ui.model

data class RepoUI(
    val id: Long,
    val name: String,
    val owner: String,
    val description: String,
    val language: String?,
    val stars: Int,
    val forks: Int,
    val openIssues: Int,
    val updatedAt: String,
    val url: String,
    val topics: List<String>
)