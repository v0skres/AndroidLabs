package com.example.androidpractice.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidpractice.AndroidPracticeApp
import com.example.androidpractice.ui.vm.FavoritesViewModel
import com.example.androidpractice.ui.vm.FavoritesViewModelFactory

@Composable
fun FavoritesScreen(
    onRepoClick: (Long) -> Unit
) {
    val app = LocalContext.current.applicationContext as AndroidPracticeApp
    val vm: FavoritesViewModel = viewModel(factory = FavoritesViewModelFactory(app.container.favoritesRepository))

    val favorites by vm.favorites.collectAsState()

    if (favorites.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("No favorites yet")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(favorites, key = { it.id }) { repo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRepoClick(repo.id) }
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(repo.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(repo.description, maxLines = 2)
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = { vm.toggleFavorite(repo) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Remove from favorites") }
                }
            }
        }
    }
}