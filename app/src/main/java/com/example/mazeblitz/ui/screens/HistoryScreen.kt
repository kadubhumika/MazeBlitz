package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mazeblitz.model.UserSession
import com.example.mazeblitz.viewmodel.MazeViewModel

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: MazeViewModel
) {

    LaunchedEffect(UserSession.playerId) {
        if (UserSession.playerId != -1) {
            viewModel.fetchHistory()
        }
    }

    val primaryBlue = Color(0xFF1E88E5)
    val softBlue = Color(0xFFE3F2FD)

    val history = viewModel.history.value
        .groupBy { "${it.level}_${it.difficulty}" }
        .map { it.value.maxByOrNull { item -> item.score }!! }
        .sortedBy { it.level }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        softBlue,
                        Color.White
                    )
                )
            )
            .padding(16.dp)
    ) {

        Text(
            text = "Game History",
            color = primaryBlue,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your performance across levels",
            color = Color(0xFF060809),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ===== CLEAR BUTTON (REPLACES NEON BUTTON) =====
        Button(
            onClick = { viewModel.deleteHistory() },
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🗑 Clear History", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {

            items(history) { item ->

                val safeDifficulty = item.difficulty ?: "easy"
                val safeDate = item.date ?: "Unknown Date"

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Level ${item.level}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )

                            Text(
                                text = safeDate,
                                fontSize = 11.sp,
                                color = Color(0xFF90A4AE)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = safeDifficulty.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (safeDifficulty.lowercase()) {
                                "easy" -> Color(0xFF43A047)
                                "hard" -> Color(0xFFE53935)
                                else -> Color(0xFFFFA000)
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = Color(0xFFE0E0E0))

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text(
                                    text = "TIME",
                                    fontSize = 11.sp,
                                    color = Color(0xFF070809)
                                )
                                Text(
                                    text = "${item.time_taken}s",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "SCORE",
                                    fontSize = 11.sp,
                                    color = Color(0xFF0C1011)
                                )
                                Text(
                                    text = "${item.score}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}