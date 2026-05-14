package com.example.androidpractice.ui.mock

import com.example.androidpractice.ui.model.RepoUI

object RepoMock {
    val items: List<RepoUI> = List(30) { idx ->
        RepoUI(
            id = idx.toLong(),
            name = "repository-$idx",
            owner = if (idx % 2 == 0) "octocat" else "androiddev",
            description = "lalala $idx bebebe",
            language = listOf("Kotlin", "Java", "Swift", "Go").getOrNull(idx % 4),
            stars = 1200 + idx * 37,
            forks = 100 + idx * 5,
            openIssues = idx % 12,
            updatedAt = "2025-12-${(idx % 28) + 1}",
            url = "https://github.com/sample/awesome-repo-$idx",
            topics = listOf("compose", "mvvm", "navigation", "android")
                .shuffled()
                .take((idx % 4) + 1)
        )
    }
}