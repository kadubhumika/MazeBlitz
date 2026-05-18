package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mazeblitz.ui.components.NeonButton

@Composable
fun DifficultyScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0010), // Cyber-wine
                        Color(0xFF0D001A), // Deep dark violet
                        Color(0xFF050505)  // Near absolute black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Screen Header with tracked styling
            Text(
                text = "SELECT DIFFICULTY",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choose your challenge tier",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Easy Tier Option
            NeonButton(
                text = "EASY",
                // Pass an optional color property if your NeonButton component accepts it,
                // otherwise it will fall back to your default neon style!
            ) {
                navController.navigate("game/easy/1")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Medium Tier Option
            NeonButton(text = "MEDIUM") {
                navController.navigate("game/medium/1")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Hard Tier Option
            NeonButton(text = "HARD") {
                navController.navigate("game/hard/1")
            }
        }
    }
}
