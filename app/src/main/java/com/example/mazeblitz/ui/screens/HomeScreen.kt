package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mazeblitz.ui.components.NeonButton

@Composable
fun HomeScreen(
    navController: NavController
) {

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
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "MazeBlitz",
                color = Color.White,
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            NeonButton(text = "Play Game") {
                navController.navigate("difficulty")
            }
            Spacer(modifier = Modifier.height(16.dp))

            NeonButton(text = "Leaderboard") {
                navController.navigate("leaderboard")
            }

            Spacer(modifier = Modifier.height(16.dp))

            NeonButton(text = "See Streak") {
                navController.navigate("history")
            }
        }
    }
}