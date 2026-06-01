package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.IPTVConfig
import com.example.ui.theme.NeonRed
import com.example.ui.viewmodels.AppViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun AdminScreen(viewModel: AppViewModel) {
    val users by viewModel.getAllUsers().collectAsState(initial = emptyList<com.example.data.local.User>())
    var url by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val config = viewModel.getIptvConfig().firstOrNull()
        if (config != null) {
            url = config.serverUrl
            username = config.username
            password = config.password
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text("Administration IPTV", color = NeonRed, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("URL Serveur (http://...)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    val cleanUrl = url.trim().removeSuffix("/")
                    viewModel.insertIptvConfig(IPTVConfig(1, cleanUrl, username.trim(), password.trim(), backupUrl = ""))
                    viewModel.loadXtreamConfigAndAuth()
                    Toast.makeText(context, "Configuration sauvegardée !", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
        ) {
            Text("Sauvegarder Configuration")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Utilisateurs", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn {
            items(users) { u ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(u.name, fontWeight = FontWeight.Bold)
                        Text(u.email)
                        Text(if (u.isOnline) "En ligne" else "Hors ligne", color = if (u.isOnline) Color.Green else Color.Gray)
                    }
                }
            }
        }
    }
}
