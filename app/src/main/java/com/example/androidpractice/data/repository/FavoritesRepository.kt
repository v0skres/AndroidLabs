package com.example.androidpractice.data.repository

import com.example.androidpractice.data.db.FavoriteRepoEntity
import com.example.androidpractice.data.db.FavoritesDao
import com.example.androidpractice.ui.model.RepoUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(
    private val dao: FavoritesDao
) {
    val favoritesFlow: Flow<List<RepoUI>> =
        dao.observeAll().map { list -> list.map { it.toUi() } }

    suspend fun toggle(repo: RepoUI) {
        if (dao.isFavorite(repo.id)) dao.deleteById(repo.id)
        else dao.upsert(repo.toEntity())
    }

    suspend fun isFavorite(id: Long): Boolean = dao.isFavorite(id)

    private fun FavoriteRepoEntity.toUi(): RepoUI = RepoUI(
        id = id,
        name = name,
        owner = owner,
        description = description ?: "No description",
        language = language,
        stars = stars,
        forks = forks,
        openIssues = openIssues,
        updatedAt = updatedAt,
        url = url,
        topics = emptyList()
    )

    private fun RepoUI.toEntity(): FavoriteRepoEntity = FavoriteRepoEntity(
        id = id,
        name = name,
        owner = owner,
        description = description,
        language = language,
        stars = stars,
        forks = forks,
        openIssues = openIssues,
        updatedAt = updatedAt,
        url = url
    )
}