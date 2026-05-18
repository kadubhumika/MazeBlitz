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
fun HistoryScreen(
    navController: NavController,
    viewModel: MazeViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.fetchHistory()
    }

    val history = viewModel.history.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF800000),
                        Color(0xFF800080),
                        Color.Black
                    ) )
            )
            .padding(16.dp)
    ) {

        Text(
            text = "History",
            color = Color.White,
            fontSize = 34.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(history) { item ->

                Text(
                    text = "Level ${item.level} | Total Score-${item.score} pts",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}