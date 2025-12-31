package com.example.brainmon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.brainmon.data.Monster
import com.example.brainmon.ui.MonstersViewModel
import com.example.brainmon.ui.components.GameBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen( // <--- RENAMED
    viewModel: MonstersViewModel,
    onAddMonsterClicked: () -> Unit,
    onMonsterClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val monsterList by viewModel.monstersUiState.collectAsState()

    val uniqueCaught = monsterList.map { it.originalName.lowercase() }.distinct().size
    val totalAvailable = 151
    val progressText = "$uniqueCaught / $totalAvailable"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Pokedex",
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                        Text(
                            text = "Caught: $progressText",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMonsterClicked) {
                Icon(Icons.Default.Add, contentDescription = "Explore")
            }
        }
    ) { innerPadding ->

        GameBackground {
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                if (monsterList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Your Pokedex is empty. Go explore!")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp, start = 8.dp, end = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val sortedList = monsterList.sortedBy {
                            viewModel.getRealPokedexNumber(it.originalName)
                        }

                        items(sortedList) { monster ->
                            val realId = viewModel.getRealPokedexNumber(monster.originalName)

                            MonsterGridItem(
                                monster = monster,
                                displayId = realId,
                                onClick = { onMonsterClick(monster.id.toString()) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonsterGridItem(monster: Monster, displayId: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "#${displayId.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
            AsyncImage(
                model = monster.imageUrl,
                contentDescription = monster.name,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = monster.originalName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}