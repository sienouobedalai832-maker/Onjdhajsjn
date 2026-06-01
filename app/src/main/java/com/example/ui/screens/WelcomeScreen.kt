package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonRed
import com.example.ui.viewmodels.AppViewModel

@Composable
fun WelcomeScreen(onSkip: () -> Unit, viewModel: AppViewModel) {
    val user by viewModel.currentUser.collectAsState()
    
    LaunchedEffect(user) {
        if (user != null) {
            onSkip()
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Ciné BOX",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = NeonRed,
                modifier = Modifier.alpha(alpha)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Créée par Obedalaï 🇧🇫",
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "t.me/Sienouobedalai832",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "TOUT LE CONTENU EST 100% GRATUIT",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onSkip,
                colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Passer", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
