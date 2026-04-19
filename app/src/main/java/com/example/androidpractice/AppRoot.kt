package com.example.androidpractice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.androidpractice.ui.screens.ProfileScreen
import com.example.androidpractice.ui.screens.EditProfileScreen
import com.example.androidpractice.ui.vm.ProfileViewModel
import com.example.androidpractice.ui.vm.ProfileViewModelFactory
import com.example.androidpractice.data.repository.impl.ProfileRepositoryImpl
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.androidpractice.ui.screens.FavoritesScreen
import com.example.androidpractice.ui.screens.RepoDetailsScreen
import com.example.androidpractice.ui.screens.ReposListScreen
import com.example.androidpractice.ui.screens.SettingsRoute
import com.example.androidpractice.ui.vm.*
import android.widget.Toast

sealed class BottomDest(val route: String, val label: String) {
    data object Repos : BottomDest("repos_list", "Repos")
    data object Favorites : BottomDest("favorites", "Fav")
    //data object Settings : BottomDest("settings", "Settings")
    data object Profile : BottomDest("profile", "Profile")   // новый пункт
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()

    val ctx = LocalContext.current
    val container = remember {
        AppContainer(ctx.applicationContext)
    }


    val settingsVm: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(ctx))
    val filters = settingsVm.settings.collectAsState().value

    val favoritesVm: FavoritesViewModel =
        viewModel(factory = FavoritesViewModelFactory(container.favoritesRepository))

    val favoritesIds = favoritesVm.favorites.collectAsState().value.map { it.id }.toSet()

    val profileRepository = remember {
        ProfileRepositoryImpl(ctx.applicationContext)
    }
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(profileRepository)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                listOf(BottomDest.Repos, BottomDest.Favorites, BottomDest.Profile).forEach { dest ->
                    val icon = when (dest) {
                        BottomDest.Repos -> Icons.AutoMirrored.Filled.List
                        BottomDest.Favorites -> Icons.Default.Star
                        //BottomDest.Settings -> Icons.Default.Settings
                        BottomDest.Profile -> Icons.Default.Person
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

            composable(BottomDest.Repos.route) {
                val reposVm: RepoViewModel = viewModel()

                LaunchedEffect(Unit) {
                    reposVm.load(owner = "google")
                }

                ReposListScreen(
                    state = reposVm.state.collectAsState().value,
                    onRetry = { reposVm.load(owner = filters.owner) },
                    onRepoClick = { id -> navController.navigate("repo_details/$id") },
                    favoritesIds = favoritesIds,
                    onToggleFavorite = { repoUi -> favoritesVm.toggleFavorite(repoUi) }
                )
            }

            composable(
                "repo_details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { entry ->
                val reposVm: RepoViewModel = viewModel()
                val id = entry.arguments?.getLong("id") ?: -1L

                RepoDetailsScreen(
                    id = id,
                    reposVm = reposVm,
                    favoritesVm = favoritesVm,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(BottomDest.Favorites.route) {
                FavoritesScreen(
                    onRepoClick = { id -> navController.navigate("repo_details/$id") }
                )
            }

            //composable(BottomDest.Settings.route) {
            //    SettingsRoute(
            //        onApplied = { navController.navigate(BottomDest.Repos.route) }
            //    )
            //}

            composable(BottomDest.Profile.route) {
                val ctx = LocalContext.current
                val profileRepository = remember {
                    ProfileRepositoryImpl(ctx.applicationContext)
                }
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(profileRepository)
                )
                val profileState by profileViewModel.profile.collectAsState()

                ProfileScreen(
                    viewModel = profileViewModel,
                    onEditClick = { navController.navigate("edit_profile") },
                    onResumeClick = {
                        profileViewModel.downloadAndOpenResume(
                            url = profileState.resumeUrl,
                            context = ctx,
                            onError = { error ->
                                Toast.makeText(ctx, error, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                )
            }

            composable("edit_profile") {
                val ctx = LocalContext.current
                val profileRepository = remember {
                    ProfileRepositoryImpl(ctx.applicationContext)
                }
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(profileRepository)
                )
                val profileState by profileViewModel.profile.collectAsState()

                EditProfileScreen(
                    initialProfile = profileState,
                    onSave = { newProfile ->
                        profileViewModel.saveProfile(newProfile)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}