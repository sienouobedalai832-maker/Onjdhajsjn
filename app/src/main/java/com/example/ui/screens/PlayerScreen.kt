package com.example.ui.screens

import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.CineBoxExoPlayer
import com.example.ui.components.CineBoxWebView

@Composable
fun PlayerScreen(
    encodedUrl: String,
    onBack: () -> Unit
) {
    val url = java.net.URLDecoder.decode(encodedUrl, "UTF-8")
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val activity = context as? ComponentActivity
        val originalOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        
        onDispose {
            activity?.requestedOrientation = originalOrientation
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (url.contains(".m3u8") || url.contains(".ts")) {
            CineBoxExoPlayer(url = url)
        } else {
            CineBoxWebView(url = url, onBackRequired = onBack)
        }
        
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
    }
}
