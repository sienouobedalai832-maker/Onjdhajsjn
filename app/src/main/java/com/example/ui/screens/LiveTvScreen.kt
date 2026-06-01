package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.remote.XtreamStream
import com.example.ui.theme.NeonRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodels.AppViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun LiveTvScreen(
    viewModel: AppViewModel,
    onPlayContent: (url: String, title: String) -> Unit
) {
    val authStatus by viewModel.xtreamAuth.collectAsState()
    val categories by viewModel.liveCategories.collectAsState()
    val streams by viewModel.liveStreams.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (authStatus == null) {
            viewModel.loadXtreamConfigAndAuth()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "TV Direct",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (authStatus == null) {
            CircularProgressIndicator(color = NeonRed, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (authStatus == false) {
            Text(
                "Le serveur TV (Xtream) est actuellement hors ligne ou expiré.",
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(streams.take(60)) { stream -> // taking 60 for performance, can add search later
                    StreamCard(stream) {
                        scope.launch {
                            val config = viewModel.getIptvConfig()?.firstOrNull()
                            if (config != null) {
                                val url = "${config.serverUrl}/live/${config.username}/${config.password}/${stream.streamId}.m3u8"
                                onPlayContent(java.net.URLEncoder.encode(url, "UTF-8"), stream.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StreamCard(stream: XtreamStream, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = stream.streamIcon ?: "",
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stream.name,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
