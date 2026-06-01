package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodels.AppViewModel

@Composable
fun ProfileScreen(viewModel: AppViewModel, onLogout: () -> Unit) {
    val user by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Surface(
            shape = CircleShape,
            color = SurfaceDark,
            modifier = Modifier.size(100.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.padding(24.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = user?.name ?: "Utilisateur", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = user?.email ?: "", color = Color.Gray, fontSize = 16.sp)
        
        Spacer(modifier = Modifier.height(16.dp))
        Badge(containerColor = Color.Green) {
            Text("ACCÈS GRATUIT ILLIMITÉ", color = Color.Black, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                viewModel.logout()
                onLogout()
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
        ) {
            Text("Déconnexion", color = Color.White)
        }
    }
}
