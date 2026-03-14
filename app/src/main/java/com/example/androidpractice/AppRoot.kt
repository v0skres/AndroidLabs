package com.example.androidpractice.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.androidpractice.ui.screens.PlaceholderScreen
import com.example.androidpractice.ui.screens.DetailsScreen
import com.example.androidpractice.ui.screens.ListScreen
import com.example.androidpractice.ui.vm.ReposViewModel
import androidx.compose.foundation.layout.padding


sealed class BottomDest(val route: String, val label: String) {
    data object Repos : BottomDest("repos_list", "Repos")
    data object Favorites : BottomDest("favorites", "Fav")
    data object Settings : BottomDest("settings", "Settings")
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val bottomItems = listOf(
        BottomDest.Repos,
        BottomDest.Favorites,
        BottomDest.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                bottomItems.forEach { dest ->
                    val icon = when (dest) {
                        BottomDest.Repos -> Icons.Default.List
                        BottomDest.Favorites -> Icons.Default.Star
                        BottomDest.Settings -> Icons.Default.Settings
                    }

                    NavigationBarItem(
                        selected = currentRoute == dest.route,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(BottomDest.Repos.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = dest.label) },
                        label = { Text(dest.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomDest.Repos.route,
            modifier = Modifier.padding(padding)
        ) {
            composable("repos_list") {
                val vm: ReposViewModel = viewModel()
                ListScreen(
                    vm = vm,
                    onRepoClick = { id ->
                        navController.navigate("repo_details/$id")
                    }
                )
            }

            composable(
                "repo_details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { entry ->
                val vm: ReposViewModel = viewModel()
                val id = entry.arguments?.getLong("id") ?: -1L
                DetailsScreen(
                    repo = vm.getById(id),
                    onBack = { navController.popBackStack() }
                )
            }

            composable("favorites") { PlaceholderScreen("Favorites") }
            composable("settings") { PlaceholderScreen("Settings") }
        }
    }
}