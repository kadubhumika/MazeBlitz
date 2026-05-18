package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mazeblitz.ui.components.MazeCell
import com.example.mazeblitz.ui.components.NeonButton
import com.example.mazeblitz.viewmodel.MazeViewModel
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    navController: NavController,
    difficulty: String,
    level: Int,
    viewModel: MazeViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.generateMaze(difficulty, level)
    }

    val maze = viewModel.mazeResponse.value

    var playerRow by remember { mutableStateOf(0) }
    var playerCol by remember { mutableStateOf(0) }
    var steps by remember { mutableStateOf(0) }
    var timer by remember { mutableStateOf(60) }
    var isGameOver by remember { mutableStateOf(false) }

    val visitedCells = remember { mutableStateListOf<Pair<Int, Int>>() }

    // Reset player position when a new maze loads
    LaunchedEffect(maze) {
        maze?.maze_data?.maze?.let {
            playerRow = 0
            playerCol = 0
            steps = 0
            isGameOver = false
            visitedCells.clear()
            visitedCells.add(Pair(0, 0))
        }
    }

    // FIXED TIMER: Single long-running coroutine loop
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (timer > 0) {
                delay(1000L)
                timer--
            }
            if (timer == 0 && !isGameOver) {
                isGameOver = true
                // Handle timeout navigation here if needed, e.g., to a game-over screen
            }
        }
    }

    fun movePlayer(dr: Int, dc: Int) {
        if (isGameOver) return // Prevent inputs if game won or timed out

        val mazeGrid = maze?.maze_data?.maze ?: return
        val newRow = playerRow + dr
        val newCol = playerCol + dc

        if (newRow in mazeGrid.indices && newCol in mazeGrid[0].indices) {
            val cell = mazeGrid[newRow][newCol]

            // 0 is path, "S" is Start, "E" is End. Block only walls (1 or 1.0)
            if (cell != 1.0 && cell != 1) {
                playerRow = newRow
                playerCol = newCol
                steps++

                visitedCells.add(Pair(newRow, newCol))

                // FIXED NAVIGATION: Block duplicate triggers
                if (cell.toString() == "E") {
                    isGameOver = true
                    navController.navigate("result/$difficulty/$level/$steps") {
                        // Pops the game screen so pressing back doesn't re-enter the finished game
                        popUpTo("game/$difficulty/$level") { inclusive = true }
                    }
                }
            }
        }
    }

    val backgroundGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF0B0C10), Color(0xFF1F2833))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Difficulty: ${difficulty.replaceFirstChar { it.uppercase() }}",
            color = Color.White,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Time Left: $timer",
            color = if (timer < 10) Color.Red else Color.Yellow, // Visual alert for low time
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            maze?.maze_data?.maze?.let { grid ->
                val totalRows = grid.size
                val totalCols = grid[0].size
                val flattenedGrid = remember(grid) { grid.flatten() }

                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp - 32.dp
                val screenHeight = configuration.screenHeightDp.dp * 0.45f

                val optimalCellSizeByWidth = screenWidth / totalCols
                val optimalCellSizeByHeight = screenHeight / totalRows
                val finalCellSize = minOf(optimalCellSizeByWidth, optimalCellSizeByHeight).coerceIn(10.dp, 40.dp)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(totalCols),
                    modifier = Modifier
                        .width(finalCellSize * totalCols)
                        .height(finalCellSize * totalRows),
                    userScrollEnabled = false
                ) {
                    items(flattenedGrid.size) { index ->
                        val row = index / totalCols
                        val col = index % totalCols
                        val cell = flattenedGrid[index]
                        val isPlayer = row == playerRow && col == playerCol
                        val isVisited = visitedCells.contains(Pair(row, col))

                        MazeCell(
                            cell = cell,
                            isPlayer = isPlayer,
                            isVisited = isVisited,
                            cellSize = finalCellSize
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Control Layout
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            NeonButton(text = "⬆") { movePlayer(-1, 0) }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                NeonButton(text = "⬅") { movePlayer(0, -1) }
                NeonButton(text = "➡") { movePlayer(0, 1) }
            }

            Spacer(modifier = Modifier.height(10.dp))

            NeonButton(text = "⬇") { movePlayer(1, 0) }
        }
    }
}
