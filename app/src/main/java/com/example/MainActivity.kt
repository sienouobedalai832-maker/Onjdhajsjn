package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.AppDatabase
import com.example.data.repository.CineBoxRepository
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.CineBoxTheme
import com.example.ui.viewmodels.AppViewModel
import com.example.ui.viewmodels.AppViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CineBoxRepository(database.cineBoxDao())
        val viewModelFactory = AppViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[AppViewModel::class.java]

        val sharedPreferences = getSharedPreferences("CinePrimePrefs", MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("USER_EMAIL", null)
        
        if (savedEmail != null) {
            viewModel.checkAutoLogin(savedEmail)
        }

        setContent {
            CineBoxTheme {
                AppNavigation(viewModel = viewModel, onLoginSuccess = { email -> 
                    sharedPreferences.edit().putString("USER_EMAIL", email).apply()
                }, onLogout = {
                    sharedPreferences.edit().remove("USER_EMAIL").apply()
                })
            }
        }
    }
}
