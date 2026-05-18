package com.example.mazeblitz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MazeCell(
    cell: Any,
    isPlayer: Boolean,
    isVisited: Boolean, // Tracks if user walked here
    cellSize: Dp
) {
    val backgroundColor = when {
        // Wall
        cell == 1.0 || cell == 1 -> Color(0xFFDCCA2B)

        // Start
        cell == "S" -> Color(0xFF2ECC71)

        // End
        cell == "E" -> Color(0xFFE74C3C)

        // Path already crossed by player (Turn Green)
        isVisited -> Color(0xFF2ECC71).copy(alpha = 0.6f)

        // Unvisited Path
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(cellSize)
            .background(backgroundColor)
            .border(0.5.dp, Color(0x1A66FCF1)),
        contentAlignment = Alignment.Center
    ) {
        if (isPlayer) {
            Text(
                text = "🏃‍♂️",
                fontSize = (cellSize.value * 0.7f).sp // Scaled dynamically to fit cell box size
            )
        }
    }
}
