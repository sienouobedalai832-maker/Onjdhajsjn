package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Accueil", Icons.Default.Home)
    object LiveTv : Screen("livetv", "TV Direct", Icons.Default.LiveTv)
    object Profile : Screen("profile", "Profil", Icons.Default.Person)
    object Admin : Screen("admin", "Admin", Icons.Default.Settings)
}

object Routes {
    const val WELCOME = "welcome"
    const val AUTH = "auth"
    const val MAIN = "main"
    const val MOVIE_DETAIL = "movie_detail/{id}"
    const val SERIE_DETAIL = "serie_detail/{id}"
    const val PLAYER = "player/{url}"
}
