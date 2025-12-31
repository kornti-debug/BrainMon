package com.example.brainmon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.brainmon.ui.MonstersViewModel
import com.example.brainmon.data.Monster
import com.example.brainmon.ui.components.GameBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabScreen(
    monsterId: String?,
    viewModel: MonstersViewModel,
    onNavigateBack: () -> Unit
) {
    val id = monsterId?.toIntOrNull() ?: 0
    val monster by viewModel.getMonsterStream(id).collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Monster Lab",
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        GameBackground { // <--- Background Wrapper
            // Note: We move the 'if monster != null' check INSIDE the background
            // so the background is always visible even while loading
            if (monster != null) {
                monster?.let { currentMonster ->
                    var newName by remember(currentMonster.id) { mutableStateOf(currentMonster.name) }

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier.size(200.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = currentMonster.imageUrl,
                                    contentDescription = "Sprite",
                                    modifier = Modifier.size(180.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("SPECIES", style = MaterialTheme.typography.labelSmall)
                                    Text(currentMonster.originalName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("TYPE", style = MaterialTheme.typography.labelSmall)
                                    Text(currentMonster.type, style = MaterialTheme.typography.titleMedium)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("CP", style = MaterialTheme.typography.labelSmall)
                                    Text(
                                        text = "${currentMonster.combatPower}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, // <--- Tech CP
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Nickname") },
                            placeholder = { Text(currentMonster.originalName) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (newName.isNotBlank()) {
                                    viewModel.updateMonsterName(currentMonster, newName)
                                    onNavigateBack()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Save Changes") }

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedButton(
                            onClick = {
                                onNavigateBack()
                                viewModel.deleteMonster(currentMonster)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Delete, "Release")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send to Professor") // <--- Nicer Text
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}