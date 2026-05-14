package com.example.androidpractice.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidpractice.data.local.FilterSettings

@Composable
fun SettingsScreen(
    current: FilterSettings,
    onApply: (FilterSettings) -> Unit
) {
    var owner by remember(current.owner) { mutableStateOf(current.owner) }
    var minStarsText by remember(current.minStars) { mutableStateOf(current.minStars.toString()) }
    var language by remember(current.language) { mutableStateOf(current.language) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Filters", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = owner,
            onValueChange = { owner = it },
            label = { Text("GitHub owner (username)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = minStarsText,
            onValueChange = { minStarsText = it.filter { ch -> ch.isDigit() } },
            label = { Text("Min stars") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = language,
            onValueChange = { language = it },
            label = { Text("Language (optional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val minStars = minStarsText.toIntOrNull() ?: 0
                onApply(
                    FilterSettings(
                        owner = owner.ifBlank { FilterSettings.DEFAULT_OWNER },
                        minStars = minStars,
                        language = language
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Done")
        }
    }
}