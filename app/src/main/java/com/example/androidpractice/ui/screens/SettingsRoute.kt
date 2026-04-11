package com.example.androidpractice.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidpractice.ui.vm.SettingsViewModel
import com.example.androidpractice.ui.vm.SettingsViewModelFactory

@Composable
fun SettingsRoute(
    onApplied: () -> Unit
) {
    val ctx = LocalContext.current
    val vm: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(ctx))

    val current = vm.settings.collectAsState().value

    SettingsScreen(
        current = current,
        onApply = { newSettings ->
            vm.save(newSettings)
            onApplied()
        }
    )
}