package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mazeblitz.viewmodel.MazeViewModel

@Composable
fun ResultScreen(
    navController: NavController,
    difficulty: String,
    level: Int,
    steps: Int,
    viewModel: MazeViewModel
) {

    val score = 1000 - (steps * 5)
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(level) {
        viewModel.submitScore(
            difficulty = difficulty,
            level = level,
            score = score,
            time = 0f,
            steps = steps
        )
        LevelManager.markLevelCompleted(
            context,
            difficulty,
            level
        )

        LevelManager.unlockLevel(
            context,
            difficulty,
            level + 1
        )
    }

    // Light professional theme colors
    val primaryBlue = Color(0xFF1E88E5)
    val softBlue = Color(0xFFE3F2FD)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        softBlue,
                        Color.White
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "🎉 Level Completed",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Great job finishing Level $level",
                    fontSize = 14.sp,
                    color = Color(0xFF607D8B),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== STATS BOX =====
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = softBlue
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("STEPS", color = Color(0xFF607D8B), fontSize = 12.sp)
                            Text("$steps", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SCORE", color = Color(0xFF607D8B), fontSize = 12.sp)
                            Text("$score", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ===== BUTTON STYLE =====
                @Composable
                fun ActionButton(text: String, onClick: () -> Unit) {
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = text,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ActionButton("Next Level") {
                        val nextLevel = level + 1
                        navController.navigate("game/$difficulty/$nextLevel")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Button(
                            onClick = { navController.navigate("leaderboard") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(primaryBlue)
                        ) {
                            Text("Leaderboard", fontSize = 13.sp)
                        }

                        Button(
                            onClick = { navController.navigate("history") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(primaryBlue)
                        ) {
                            Text("History", fontSize = 13.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = { navController.navigate("difficulty") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Home", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}