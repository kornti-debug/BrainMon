package com.example.brainmon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.brainmon.ui.MonstersViewModel
import com.example.brainmon.ui.components.GameBackground

// Removed 'capacity' from here because we now get it from the ViewModel
data class BiomeData(
    val id: String,
    val name: String,
    val subject: String,
    val description: String,
    val color1: Color,
    val color2: Color,
    val unlockCount: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: MonstersViewModel,
    onBiomeSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    // 1. WATCH THE LIVE DATA
    // We observe the list. Whenever the DB updates, 'monsters' updates.
    val monsters by viewModel.monstersUiState.collectAsState()
    val totalCount by viewModel.totalCaught.collectAsState()

    val biomes = listOf(
        // Tier 1
        BiomeData("plains", "Plains", "General", "General Knowledge", Color(0xFF81C784), Color(0xFFAED581), 0),
        BiomeData("forest", "Forest", "General", "General Knowledge", Color(0xFF2E7D32), Color(0xFF66BB6A), 5),

        // Tier 2
        BiomeData("beach", "Beach", "Biology", "Biology (Easy)", Color(0xFF29B6F6), Color(0xFF81D4FA), 15),
        BiomeData("desert", "Desert", "Math", "Math (Medium)", Color(0xFFFFB74D), Color(0xFFFFE0B2), 25),
        BiomeData("cave", "Cave", "History", "History (Medium)", Color(0xFF8D6E63), Color(0xFFA1887F), 40),

        // Tier 3
        BiomeData("swamp", "Swamp", "Biology", "Biology (Medium)", Color(0xFF5D4037), Color(0xFF795548), 55),
        BiomeData("city", "City", "Science", "Science (Medium)", Color(0xFF90A4AE), Color(0xFFCFD8DC), 70),
        BiomeData("tundra", "Tundra", "Biology", "Biology (Hard)", Color(0xFF4FC3F7), Color(0xFFB3E5FC), 90),

        // Tier 4
        BiomeData("volcano", "Volcano", "Math", "Math (Hard)", Color(0xFFD32F2F), Color(0xFFEF5350), 110),
        BiomeData("mystic", "Mystic Realm", "Myth", "Mythology (Hard)", Color(0xFF7B1FA2), Color(0xFFBA68C8), 130)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "World Map",
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Total Caught: $totalCount", style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        GameBackground {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(biomes) { biome ->

                    // --- THE FIX ---

                    // 1. Get Capacity from VM (static map)
                    val capacity = viewModel.getBiomeCapacity(biome.id)

                    // 2. Get Progress using the LIVE 'monsters' list
                    // This forces a refresh because 'monsters' changes when you catch something.
                    val (currentCount, isComplete) = viewModel.getBiomeStatus(biome.id, monsters)

                    val isLocked = totalCount < biome.unlockCount
                    val statusText = if(isComplete) "COMPLETE" else "$currentCount / $capacity"

                    BiomeGridCard(
                        biome = biome,
                        isLocked = isLocked,
                        statusText = statusText,
                        isComplete = isComplete,
                        onClick = {
                            if (!isLocked && !isComplete) {
                                onBiomeSelected(biome.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BiomeGridCard(
    biome: BiomeData,
    isLocked: Boolean,
    statusText: String,
    isComplete: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(160.dp)
            .clickable(enabled = !isLocked) { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isLocked) Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
                    else if (isComplete) Brush.linearGradient(listOf(Color(0xFFFFD700), Color(0xFFFFA000)))
                    else Brush.verticalGradient(listOf(biome.color1, biome.color2))
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = if (isLocked) "LOCKED" else biome.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (isLocked) {
                        Icon(Icons.Default.Lock, "Locked", tint = Color.White, modifier = Modifier.size(20.dp))
                    } else if (isComplete) {
                        Icon(Icons.Default.Star, "Mastered", tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                }

                Column {
                    if (!isLocked) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text(biome.subject, style = MaterialTheme.typography.labelSmall) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.Black.copy(alpha = 0.2f),
                                labelColor = Color.White
                            ),
                            border = null,
                            modifier = Modifier.height(24.dp).padding(bottom = 4.dp)
                        )
                    }

                    val subText = when {
                        isLocked -> "Req: ${biome.unlockCount}"
                        isComplete -> "MASTERED!"
                        else -> "Caught: $statusText"
                    }

                    Text(
                        text = subText,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}