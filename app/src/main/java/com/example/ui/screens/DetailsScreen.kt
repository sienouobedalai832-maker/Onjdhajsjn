package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.data.remote.CastMember
import com.example.data.remote.MediaItem
import com.example.ui.screens.MediaCard
import com.example.ui.theme.SurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    id: Int,
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onPlay: (url: String, title: String) -> Unit,
    onMovieClick: (Int) -> Unit = {}
) {
    var movie by remember { mutableStateOf<MovieDetail?>(null) }
    var cast by remember { mutableStateOf<List<CastMember>>(emptyList()) }
    var similar by remember { mutableStateOf<List<MediaItem>>(emptyList()) }

    LaunchedEffect(id) {
        try {
            movie = viewModel.getMovieDetail(id)
            cast = viewModel.getCredits(id)
            similar = viewModel.getSimilar(id)
            movie?.let {
                viewModel.saveHistory(id, it.title, "https://image.tmdb.org/t/p/w500${it.posterPath}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
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
            
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Lecteurs Vidéo", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Button(
                        onClick = {
                            val url = "https://frembed.bond/api/film.php?id=${m.id}"
                            onPlay(java.net.URLEncoder.encode(url, "UTF-8"), m.title)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Source 1 (Gratuit)", fontWeight = FontWeight.Bold)
                    }
                    
                    Button(
                        onClick = {
                            val url = "https://frembed.xyz/api/film.php?id=${m.id}"
                            onPlay(java.net.URLEncoder.encode(url, "UTF-8"), m.title)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Source 2 (Gratuit)", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val url = "https://frembed.live/api/film.php?id=${m.id}"
                            onPlay(java.net.URLEncoder.encode(url, "UTF-8"), m.title)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Source 3 (Gratuit)", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val url = "https://superembed.stream/movie?tmdb=${m.id}"
                            onPlay(java.net.URLEncoder.encode(url, "UTF-8"), m.title)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SuperEmbed (EN/FR)", fontWeight = FontWeight.Bold)
                    }
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
            
            if (cast.isNotEmpty()) {
                Text(
                    text = "Acteurs",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cast.take(10)) { actor ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(100.dp)) {
                            AsyncImage(
                                model = if (actor.profilePath != null) "https://image.tmdb.org/t/p/w200${actor.profilePath}" else "",
                                contentDescription = actor.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(100.dp).background(Color.DarkGray, RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = actor.name, color = Color.White, fontSize = 12.sp, maxLines = 1, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            Text(text = actor.character, color = Color.Gray, fontSize = 10.sp, maxLines = 1, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            if (similar.isNotEmpty()) {
                Text(
                    text = "Films Similaires",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(similar.take(10)) { similarMovie ->
                        MediaCard(item = similarMovie, onClick = onMovieClick)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
