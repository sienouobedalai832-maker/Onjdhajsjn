package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.remote.MovieDetail
import com.example.ui.theme.NeonRed
import com.example.ui.viewmodels.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    id: Int,
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onPlay: (url: String, title: String) -> Unit
) {
    var movie by remember { mutableStateOf<MovieDetail?>(null) }

    LaunchedEffect(id) {
        movie = viewModel.getMovieDetail(id)
        movie?.let {
            viewModel.saveHistory(id, it.title, "https://image.tmdb.org/t/p/w500${it.posterPath}")
        }
    }

    if (movie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NeonRed)
        }
        return
    }

    val m = movie!!

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original${m.backdropPath}",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().align(Alignment.TopCenter),
            alpha = 0.3f
        )
        
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Row(modifier = Modifier.padding(16.dp)) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${m.posterPath}",
                    contentDescription = null,
                    modifier = Modifier.width(140.dp).height(210.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = m.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = m.releaseDate, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "⭐ ${m.voteAverage}", color = Color.Yellow)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = m.genres.joinToString(", ") { it.name }, color = Color.LightGray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        val url = "https://frembed.bond/api/film.php?id=${m.id}"
                        onPlay(java.net.URLEncoder.encode(url, "UTF-8"), m.title)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("VF", fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = {
                        val url = "https://autoembed.cc/embed/movie/${m.id}"
                        onPlay(java.net.URLEncoder.encode(url, "UTF-8"), m.title)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("VO", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Synopsis",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = m.overview,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Add actors logic and similar movies later if needed
        }
    }
}
