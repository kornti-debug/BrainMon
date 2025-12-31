package com.example.brainmon.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.brainmon.ui.MonstersViewModel
import com.example.brainmon.ui.SoundManager
import com.example.brainmon.ui.components.GameBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleScreen(
    viewModel: MonstersViewModel,
    onCatchClicked: () -> Unit
) {
    val encounter by viewModel.currentEncounter.collectAsState()
    val questionState by viewModel.currentQuestion.collectAsState()
    val searchError by viewModel.searchError.collectAsState()

    val battleDifficulty by viewModel.battleDifficulty.collectAsState()
    val battleCategory by viewModel.battleCategory.collectAsState()
    val battleCp by viewModel.battleCp.collectAsState()
    val totalCaught by viewModel.totalCaught.collectAsState()

    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    val unlockMilestones = listOf(5, 15, 25, 40, 55, 70, 90, 110, 130)

    fun getDifficultyColor(diff: String): Color {
        return when(diff.lowercase()) {
            "easy" -> Color(0xFF4CAF50)
            "medium" -> Color(0xFFFFC107)
            "hard" -> Color(0xFFF44336)
            else -> Color.Gray
        }
    }

    fun getCategoryColor(cat: String): Color {
        return when(cat) {
            "Math" -> Color(0xFFD32F2F)
            "Biology" -> Color(0xFF29B6F6)
            "History" -> Color(0xFF8D6E63)
            "Science" -> Color(0xFF90A4AE)
            "Mythology" -> Color(0xFF7B1FA2)
            else -> Color(0xFF66BB6A)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Wild Encounter",
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCatchClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Run Away")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->

        GameBackground {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (searchError) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
                        Text("Wild Pokemon fled...", style = MaterialTheme.typography.titleMedium)
                        Button(onClick = onCatchClicked) { Text("Return to Map") }
                    }
                } else if (encounter == null || questionState == null) {
                    CircularProgressIndicator()
                } else {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {

                        // NAME
                        Text(
                            text = encounter?.name?.replaceFirstChar { it.uppercase() } ?: "Unknown",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // IMAGE
                        Box(contentAlignment = Alignment.BottomCenter) {
                            AsyncImage(
                                model = encounter?.sprites?.front_default,
                                contentDescription = "Wild Monster",
                                modifier = Modifier.size(200.dp)
                            )
                        }

                        // CP PILL
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.padding(bottom = 24.dp)
                        ) {
                            Text(
                                text = "CP $battleCp",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        // COLORED CHIPS
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            val catColor = getCategoryColor(battleCategory)

                            // 1. OUTLINED CHIP (Category) - Fixed Type Mismatch
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        battleCategory,
                                        color = catColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color.Transparent
                                ),
                                // FIX: Use BorderStroke directly instead of SuggestionChipDefaults
                                border = BorderStroke(2.dp, catColor)
                            )

                            // 2. FILLED CHIP (Difficulty)
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        battleDifficulty.uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = getDifficultyColor(battleDifficulty)
                                ),
                                border = null
                            )
                        }

                        // QUESTION CARD
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Text(
                                    text = questionState!!.text,
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                questionState!!.answers.forEach { answer ->
                                    Button(
                                        onClick = {
                                            viewModel.checkAnswer(
                                                selectedAnswer = answer,
                                                onCorrect = {
                                                    val newTotal = totalCaught + 1
                                                    if (unlockMilestones.contains(newTotal)) {
                                                        soundManager.playUnlock()
                                                        Toast.makeText(context, "BIOME UNLOCKED!", Toast.LENGTH_LONG).show()
                                                    } else {
                                                        soundManager.playCatch()
                                                        Toast.makeText(context, "Gotcha!", Toast.LENGTH_SHORT).show()
                                                    }
                                                    viewModel.captureCurrentMonster()
                                                    onCatchClicked()
                                                },
                                                onWrong = {
                                                    soundManager.playFail()
                                                    Toast.makeText(context, "It ran away!", Toast.LENGTH_SHORT).show()
                                                    onCatchClicked()
                                                }
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(text = answer, style = MaterialTheme.typography.bodyLarge)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}