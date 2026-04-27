package com.example.androidpractice.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidpractice.ui.model.RepoUI
import com.example.androidpractice.ui.vm.RepoUIState

@Composable
fun ReposListScreen(
    state: RepoUIState,
    onRetry: () -> Unit,
    onRepoClick: (Long) -> Unit,
    favoritesIds: Set<Long>,
    onToggleFavorite: (RepoUI) -> Unit
) {
    when (state) {
        is RepoUIState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is RepoUIState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(state.message)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onRetry) { Text("Retry") }
            }
        }

        is RepoUIState.Content -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.items, key = { it.id }) { repo ->
                RepoItem(
                    repo = repo,
                    isFavorite = favoritesIds.contains(repo.id),
                    onClick = { onRepoClick(repo.id) },
                    onToggleFavorite = { onToggleFavorite(repo) }
                )
            }
        }
    }
}

@Composable
private fun RepoItem(
    repo: RepoUI,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                TextButton(onClick = onToggleFavorite) {
                    Text(if (isFavorite) "★" else "☆")
                }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = repo.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}