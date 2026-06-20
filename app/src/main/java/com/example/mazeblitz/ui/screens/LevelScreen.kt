package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LevelScreen(
    navController: NavController,
    difficulty: String
) {
    val context = LocalContext.current
    val levels = (1..70).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF5795C5),
                        Color(0xFF59ACD2),
                        Color.Black
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = "${difficulty.replaceFirstChar { it.uppercase() }} levels",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(levels) { level ->
                val unlocked = LevelManager.isLevelUnlocked(context, difficulty, level)
                val completed = LevelManager.isLevelCompleted(context, difficulty, level)

                val (cardBackground, borderColor, textColor) = when {
                    completed -> Triple(
                        Color(0xFF4CAF50).copy(alpha = 0.25f),
                        Color(0xFF4CAF50),
                        Color(0xFF81C784)
                    )
                    unlocked -> Triple(
                        Color(0xFF6200EE).copy(alpha = 0.35f),
                        Color(0xFF9D4EDD),
                        Color.White
                    )
                    else -> Triple(
                        Color.White.copy(alpha = 0.05f),
                        Color.White.copy(alpha = 0.15f),
                        Color.Gray
                    )
                }

                Card(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = if (unlocked) 2.dp else 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        // Fixed: previously "unlocked && !completed" meant a
                        // level became permanently untappable the moment it
                        // was completed once. Now completed levels stay
                        // tappable so the player can replay for a better score.
                        .clickable(enabled = unlocked) {
                            navController.navigate("game/$difficulty/$level")
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (unlocked) "$level" else "lock",
                                color = textColor,
                                fontSize = if (unlocked) 22.sp else 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            if (completed) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "done",
                                    color = Color(0xFF4CAF50),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}