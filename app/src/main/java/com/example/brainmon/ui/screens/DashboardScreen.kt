package com.example.brainmon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.brainmon.ui.components.GameBackground

@Composable
fun DashboardScreen(
    onPlayClick: () -> Unit,
    onPokedexClick: () -> Unit,
    onStatsClick: () -> Unit
) {
    GameBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // TECH TITLE
            Text(
                text = "BrainMon",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace // <--- Tech Font
            )
            Text(
                text = "Gotta Solve 'Em All!",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(64.dp))

            DashboardButton(
                title = "Explore World",
                subtitle = "Catch new monsters",
                icon = Icons.Default.Place,
                onClick = onPlayClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardButton(
                title = "My Pokedex",
                subtitle = "View your collection",
                icon = Icons.Default.List,
                onClick = onPokedexClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardButton(
                title = "Trainer Stats",
                subtitle = "Check your performance",
                icon = Icons.Default.Info,
                onClick = onStatsClick
            )
        }
    }
}

@Composable
fun DashboardButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(100.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}