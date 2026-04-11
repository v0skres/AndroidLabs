package com.example.androidpractice.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidpractice.ui.vm.FavoritesViewModel
import com.example.androidpractice.ui.vm.RepoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(
    id: Long,
    reposVm: RepoViewModel,
    favoritesVm: FavoritesViewModel,
    onBack: () -> Unit
) {
    val favorites by favoritesVm.favorites.collectAsState()

    val repo = reposVm.getById(id) ?: favorites.firstOrNull { it.id == id }

    if (repo == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Repo not found")
        }
        return
    }

    val isFav = favorites.any { it.id == repo.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(repo.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
                actions = {
                    TextButton(onClick = { favoritesVm.toggleFavorite(repo) }) {
                        Text(if (isFav) "★" else "☆")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Owner: ${repo.owner}", style = MaterialTheme.typography.titleMedium)
            Text(repo.description, style = MaterialTheme.typography.bodyMedium)
            Text("Language: ${repo.language ?: "—"}")
            Text("Stars: ${repo.stars}")
            Text("Forks: ${repo.forks}")
            Text("Open issues: ${repo.openIssues}")
            Text("Updated: ${repo.updatedAt}")

            Button(
                onClick = { favoritesVm.toggleFavorite(repo) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isFav) "Remove from favorites" else "Add to favorites")
            }
        }
    }
}