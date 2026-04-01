package com.example.androidpractice.data.repository

import com.example.androidpractice.data.api.NetworkModule
import com.example.androidpractice.ui.model.RepoUI

class RepoRepository {

    suspend fun loadRepos(owner: String): List<RepoUI> {
        val dtos = NetworkModule.githubApi.getRepos(owner = owner)

        return dtos.map {
            RepoUI(
                id = it.id,
                name = it.name,
                owner = owner,
                description = it.description ?: "No description",
                language = it.language,
                stars = it.stargazersCount,
                forks = it.forksCount,
                openIssues = it.openIssuesCount,
                updatedAt = it.updatedAt,
                url = it.htmlUrl,
                topics = it.topics ?: emptyList()
            )
        }
    }
}