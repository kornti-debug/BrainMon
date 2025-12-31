package com.example.brainmon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.brainmon.ui.MonstersViewModel
import com.example.brainmon.ui.components.GameBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: MonstersViewModel,
    onBackClick: () -> Unit
) {
    val monsters by viewModel.monstersUiState.collectAsState()

    // --- CALCULATE STATS ---
    val totalCaught = monsters.size
    val uniqueSpecies = monsters.distinctBy { it.originalName.lowercase() }.size
    val completionPercent = if (totalCaught > 0) (uniqueSpecies / 151f) * 100 else 0f

    val strongestMonster = monsters.maxByOrNull { it.combatPower }
    val totalCp = monsters.sumOf { it.combatPower }

    val typeCounts = monsters.groupingBy { it.type }.eachCount()
    val favoriteType = typeCounts.maxByOrNull { it.value }?.key ?: "None"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Trainer Stats",
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }
    ) { innerPadding ->

        GameBackground {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 1. PROGRESS CARD
                item {
                    StatCard(title = "Pokedex Completion") {
                        LinearProgressIndicator(
                            progress = { completionPercent / 100f },
                            modifier = Modifier.fillMaxWidth().height(12.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${completionPercent.toInt()}% Complete",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace // Tech font
                        )
                        Text("$uniqueSpecies / 151 Unique Species")
                    }
                }

                // 2. TOTALS CARD
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Total Catches
                        Card(modifier = Modifier.weight(1f)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Total Caught", style = MaterialTheme.typography.labelMedium)
                                Text(
                                    "$totalCaught",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                )
                            }
                        }

                        // Total Team Power
                        Card(modifier = Modifier.weight(1f)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Team Power", style = MaterialTheme.typography.labelMedium)
                                Text(
                                    "$totalCp", // <--- THE SCORE
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // 3. STRONGEST MONSTER
                item {
                    StatCard(title = "Strongest Partner") {
                        if (strongestMonster != null) {
                            Text(
                                text = strongestMonster.name,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "CP: ${strongestMonster.combatPower}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace // Tech font
                            )
                            Text("Type: ${strongestMonster.type}")
                        } else {
                            Text("No monsters caught yet!")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}