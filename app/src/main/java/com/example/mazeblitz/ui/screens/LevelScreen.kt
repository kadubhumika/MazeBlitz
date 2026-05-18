package com.example.mazeblitz.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
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
import com.example.mazeblitz.ui.screens.LevelManager

@Composable
fun LevelScreen(
    navController: NavController,
    difficulty: String
) {

    val context = LocalContext.current

    val levels = (1..10).toList()

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
            )
            .padding(16.dp)
    ) {

        Text(
            text = "$difficulty Levels",
            color = Color.White,
            fontSize = 34.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(levels) { level ->

                val unlocked =
                    LevelManager.isLevelUnlocked(
                        context,
                        difficulty,
                        level
                    )

                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .clickable {

                            if (unlocked) {

                                navController.navigate(
                                    "game/$difficulty/$level"
                                )
                            }
                        }
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (unlocked)
                                    Color(0xFF6200EE)
                                else
                                    Color.DarkGray
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text =
                                if (unlocked)
                                    "Level $level"
                                else
                                    "Locked",
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}