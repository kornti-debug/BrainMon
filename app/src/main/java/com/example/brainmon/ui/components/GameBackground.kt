package com.example.brainmon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GameBackground(
    content: @Composable () -> Unit
) {

    val brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF5F7FA),
            Color(0xFFC3CFE2)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        content()
    }
}