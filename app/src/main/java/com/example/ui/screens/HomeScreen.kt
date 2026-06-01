package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.remote.MediaItem
import com.example.ui.theme.NeonRed
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodels.AppViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onMovieClick: (Int) -> Unit,
    onSerieClick: (Int) -> Unit
) {
    val trending by viewModel.trending.collectAsState()
    val popular by viewModel.popularMovies.collectAsState()
    val scrollState = rememberScrollState()

    var activeGenre by remember { mutableStateOf("Tout") }
    val genres = listOf("Tout", "Action", "Drame", "Horreur", "Comédie", "Anime", "Science-Fiction", "Animation", "Thriller", "Aventure", "Série")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
    ) {
        var searchQuery by remember { mutableStateOf("") }
        val searchResults by viewModel.searchResults.collectAsState()

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    viewModel.searchMovies(it)
                },
                placeholder = { Text("Rechercher un film...", color = Color.Gray) },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonRed,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            
            Badge(containerColor = Color.Green) {
                Text(
                    "GRATUIT",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Animated Banner (Parallax fake) - Hide when searching
        if (searchQuery.isEmpty() && trending.isNotEmpty()) {
            BannerParallax(trending.take(5), onMovieClick)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Row
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                val isActive = genre == activeGenre
                AssistChip(
                    onClick = { 
                        activeGenre = genre
                        val genreMap = mapOf("Action" to 28, "Drame" to 18, "Horreur" to 27, "Comédie" to 35, "Anime" to 16, "Science-Fiction" to 878, "Animation" to 16, "Thriller" to 53, "Aventure" to 12)
                        if (genre != "Tout" && genreMap.containsKey(genre)) {
                            viewModel.loadMoviesByGenre(genreMap[genre]!!)
                        }
                    },
                    label = { Text(genre, color = if (isActive) Color.White else Color.Gray) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isActive) NeonRed else SurfaceDark
                    ),
                    border = null,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val genreMovies by viewModel.genreMovies.collectAsState()
        
        // Let's add extra categories like Horror and Adventure
        val horrorMovies by viewModel.horrorMovies.collectAsState()
        val adventureMovies by viewModel.adventureMovies.collectAsState()

        // Film Grids
        if (searchQuery.isNotEmpty()) {
            if (searchResults.isNotEmpty()) {
                MediaSection("Résultats pour '$searchQuery'", searchResults, onMovieClick)
            } else {
                Text(
                    text = "Aucun résultat trouvé.",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (activeGenre != "Tout" && genreMovies.isNotEmpty()) {
            MediaSection(activeGenre, genreMovies, onMovieClick)
        } else if (activeGenre == "Tout") {
            if (trending.isNotEmpty()) {
                MediaSection("Tendances", trending, onMovieClick)
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (popular.isNotEmpty()) {
                MediaSection("Films Populaires", popular, onMovieClick)
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (horrorMovies.isNotEmpty()) {
                MediaSection("Horreur", horrorMovies, onMovieClick)
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (adventureMovies.isNotEmpty()) {
                MediaSection("Aventure", adventureMovies, onMovieClick)
            }
        }

        Spacer(modifier = Modifier.height(100.dp)) // padding for bottom nav
    }
}

@Composable
fun BannerParallax(movies: List<MediaItem>, onMovieClick: (Int) -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }
    
    LaunchedEffect(movies) {
        while(true) {
            delay(4000)
            currentIndex = (currentIndex + 1) % movies.size
        }
    }
    
    val currentMovie = movies[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clickable { onMovieClick(currentMovie.id) }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/original${currentMovie.backdropPath ?: currentMovie.posterPath}",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = currentMovie.displayTitle,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onMovieClick(currentMovie.id) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
            ) {
                Text("REGARDER MAINTENANT", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MediaSection(title: String, mediaList: List<MediaItem>, onClick: (Int) -> Unit) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(mediaList) { item ->
                MediaCard(item, onClick)
            }
        }
    }
}

@Composable
fun MediaCard(item: MediaItem, onClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clickable { onClick(item.id) },
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${item.posterPath}",
            contentDescription = item.displayTitle,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
