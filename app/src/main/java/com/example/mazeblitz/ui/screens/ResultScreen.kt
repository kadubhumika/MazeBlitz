package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mazeblitz.ui.components.NeonButton
import com.example.mazeblitz.ui.screens.LevelManager

@Composable
fun ResultScreen(
    navController: NavController,
    difficulty: String,
    level: Int,
    steps: Int
) {

    val context = LocalContext.current

    val score = 1000 - (steps * 5)

    // Unlock next level
    LevelManager.unlockLevel(
        context,
        difficulty,
        level + 1
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF800000),
                        Color(0xFF800080),
                        Color.Black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🎉 Congratulations",
                color = Color.White,
                fontSize = 34.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Level $level Completed",
                color = Color.White,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Steps: $steps",
                color = Color.White,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Score: $score",
                color = Color.Yellow,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            NeonButton(text = "Next Level") {

                navController.navigate(
                    "game/$difficulty/${level + 1}"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            NeonButton(text = "Leaderboard") {

                navController.navigate(
                    "leaderboard"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            NeonButton(text = "Home") {

                navController.navigate(
                    "difficulty"
                )
            }
        }
    }
}