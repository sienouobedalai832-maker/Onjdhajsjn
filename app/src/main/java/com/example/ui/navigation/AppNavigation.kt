package com.example.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.ui.screens.*
import com.example.ui.theme.NeonRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodels.AppViewModel

@Composable
fun AppNavigation(viewModel: AppViewModel, navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) {
            WelcomeScreen(viewModel = viewModel, onSkip = {
                navController.navigate(Routes.AUTH) {
                    popUpTo(Routes.WELCOME) { inclusive = true }
                }
            })
        }
        composable(Routes.AUTH) {
            AuthScreen(viewModel = viewModel, onLoggedIn = {
                navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.AUTH) { inclusive = true }
                }
            })
        }
        composable(Routes.MAIN) {
            MainScreen(viewModel = viewModel, rootNavController = navController)
        }
        composable(Routes.MOVIE_DETAIL) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
            DetailsScreen(
                id = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPlay = { encodedUrl, _ ->
                    navController.navigate("player/$encodedUrl")
                },
                onMovieClick = { newId -> 
                    navController.navigate("movie_detail/$newId")
                }
            )
        }
        composable(Routes.PLAYER) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: return@composable
            PlayerScreen(
                encodedUrl = encodedUrl,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainScreen(viewModel: AppViewModel, rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val user by viewModel.currentUser.collectAsState()
    val isAdmin = user?.email == "sienouobedalai832@gmail.com" || user?.email == "sienouobedalai3@gmail.com"
    
    val items = mutableListOf(Screen.Home, Screen.LiveTv, Screen.Profile)
    if (isAdmin) {
        items.add(Screen.Admin)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = SurfaceDark) {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            bottomNavController.navigate(screen.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonRed,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = NeonRed,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onMovieClick = { id -> rootNavController.navigate("movie_detail/$id") },
                    onSerieClick = { id -> rootNavController.navigate("movie_detail/$id") } // We use movie details for simplicity, but can add series details
                )
            }
            composable(Screen.LiveTv.route) {
                LiveTvScreen(
                    viewModel = viewModel,
                    onPlayContent = { encodedUrl, _ -> rootNavController.navigate("player/$encodedUrl") }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel = viewModel, onLogout = {
                    rootNavController.navigate(Routes.AUTH) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                })
            }
            composable(Screen.Admin.route) {
                AdminScreen(viewModel = viewModel)
            }
        }
    }
}
