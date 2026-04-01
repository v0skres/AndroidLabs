package com.example.androidpractice.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidpractice.ui.model.RepoUI

@Composable
fun DetailsScreen(
    repo: RepoUI?,
    onBack: () -> Unit
) {
    if (repo == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Repo not found")
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = onBack) {
                Text("Back")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = repo.name,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = repo.description)
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}