package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mazeblitz.viewmodel.MazeViewModel

@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: MazeViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.fetchLeaderboard()
    }

    val dummy_leaderboard = listOf(
        Pair("Alex", 1200),
        Pair("Bhumika", 1100),
        Pair("Rahul", 950),
        Pair("Neha", 900),
        Pair("John", 850)
    )

    Column(
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
            ).padding(16.dp)
    ) {

        Text(
            text = "Leaderboard",
            color = Color.White,
            fontSize = 34.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val finalLeaderboard = mutableListOf<Pair<String, Int>>()
            val leaderboard = viewModel.leaderboard.value

// Backend scores
            leaderboard.forEachIndexed { index, item ->

                finalLeaderboard.add(
                    Pair(
                        item.username,
                        item.score
                    )
                )
            }

// Dummy scores
            finalLeaderboard.addAll(dummy_leaderboard)


            items(finalLeaderboard) { item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray)
                        .padding(16.dp),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text = item.first,
                        color = Color.White,
                        fontSize = 22.sp
                    )

                    Text(
                        text = item.second.toString(),
                        color = Color.Yellow,
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}