package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mazeblitz.model.MazeCell
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
    val maze = viewModel.mazeResponse.value

    var playerRow by remember { mutableStateOf(0) }
    var playerCol by remember { mutableStateOf(0) }
    var steps by remember { mutableStateOf(0) }
    var timer by remember { mutableStateOf(60) }
    var scoreSubmitted by remember { mutableStateOf(false) }
    var startTime by rememberSaveable { mutableStateOf(0L) }
    var isGameOver by remember { mutableStateOf(false) }
    val visitedCells = remember { mutableStateListOf<Pair<Int, Int>>() }

    var wallHitStreak by remember { mutableStateOf(0) }
    val maxWallHits = 2

    var trapsTriggered by remember { mutableStateOf(0) }
    var warningMessage by remember { mutableStateOf("") }
    var scoreSubmitError by remember { mutableStateOf(false) }

    LaunchedEffect(difficulty, level) {
        viewModel.generateMaze(difficulty, level)
        playerRow = 0
        playerCol = 0
        steps = 0
        isGameOver = false
        visitedCells.clear()
        visitedCells.add(Pair(0, 0))
        scoreSubmitted = false
        startTime = 0L
        wallHitStreak = 0
        trapsTriggered = 0
        warningMessage = ""
        scoreSubmitError = false
    }

    LaunchedEffect(maze) {
        maze?.let {
            timer = it.timer
            if (startTime == 0L) {
                startTime = System.currentTimeMillis()
            }
        }
    }

    LaunchedEffect(isGameOver, maze) {
        if (maze != null && !isGameOver) {
            while (timer > 0 && !isGameOver) {
                delay(1000L)
                timer--
            }
            if (timer == 0) {
                isGameOver = true
                warningMessage = "Time's up"
            }
        }
    }

    fun movePlayer(dr: Int, dc: Int) {
        if (isGameOver) return

        val mazeGrid = maze?.maze_data?.maze ?: return

        val newRow = playerRow + dr
        val newCol = playerCol + dc

        if (newRow !in mazeGrid.indices || newCol !in mazeGrid[0].indices) {
            return
        }

        when (val cell = mazeGrid[newRow][newCol]) {

            is MazeCell.Wall -> {
                wallHitStreak++
                warningMessage = "Wall hit ($wallHitStreak/$maxWallHits)"

                if (wallHitStreak >= maxWallHits) {
                    playerRow = 0
                    playerCol = 0
                    wallHitStreak = 0
                    warningMessage = "Too many wall hits in a row - back to start"
                    visitedCells.clear()
                    visitedCells.add(Pair(0, 0))
                }
            }

            is MazeCell.Path, is MazeCell.Trap, is MazeCell.Start, is MazeCell.End -> {
                wallHitStreak = 0
                playerRow = newRow
                playerCol = newCol
                steps++
                visitedCells.add(Pair(newRow, newCol))

                if (cell is MazeCell.Trap) {
                    trapsTriggered++
                    timer = maxOf(0, timer - 5)
                    warningMessage = "Trap! -5 seconds"

                    if (timer == 0) {
                        isGameOver = true
                        warningMessage = "Time's up"
                        return
                    }
                } else {
                    warningMessage = ""
                }

                if (cell is MazeCell.End && !scoreSubmitted) {
                    isGameOver = true
                    scoreSubmitted = true

                    val timeTaken = (System.currentTimeMillis() - startTime) / 1000f

                    LevelManager.markLevelCompleted(navController.context, difficulty, level)

                    viewModel.submitScore(
                        difficulty = difficulty,
                        level = level,
                        score = maxOf(10, (timer * 10) - (steps * 2) - (trapsTriggered * 5)),
                        time = timeTaken,
                        steps = steps,
                        onError = { scoreSubmitError = true }
                    )

                    navController.navigate("result/$difficulty/$level/$steps") {
                        popUpTo("game/$difficulty/$level") {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF1A2A4A), Color(0xFF0D1830), Color(0xFF0A1426))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${difficulty.replaceFirstChar { it.uppercase() }} maze - Lv. $level",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            HudStat(
                label = "Time left",
                value = "${timer}s",
                valueColor = when {
                    timer <= 10 -> Color(0xFFE05A5A)
                    timer <= 25 -> Color(0xFFE7CF08)
                    else -> Color(0xFF5DCAA5)
                },
                modifier = Modifier.weight(1f)
            )
            HudStat(
                label = "Steps",
                value = "$steps",
                valueColor = Color.White,
                modifier = Modifier.weight(1f)
            )
            HudStat(
                label = "Wall streak",
                value = "$wallHitStreak/$maxWallHits",
                valueColor = if (wallHitStreak > 0) Color(0xFFF0997B) else Color.White,
                modifier = Modifier.weight(1f)
            )
        }

        if (warningMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .background(Color(0x33E05A5A), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = warningMessage,
                    color = Color(0xFFF09595),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (scoreSubmitError) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Score couldn't be saved to the server - your local progress is still kept.",
                color = Color(0xFFF0997B),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            maze?.maze_data?.maze?.let { grid ->
                val totalRows = grid.size
                val totalCols = grid[0].size

                val configuration = LocalConfiguration.current
                val density = LocalDensity.current
                val screenWidthPx = with(density) {
                    (configuration.screenWidthDp.dp - 40.dp).toPx()
                }
                val screenHeightPx = with(density) {
                    (configuration.screenHeightDp.dp * 0.45f).toPx()
                }

                val cellSizePx = minOf(screenWidthPx / totalCols, screenHeightPx / totalRows)
                val mazeWidthDp = with(density) { (cellSizePx * totalCols).toDp() }
                val mazeHeightDp = with(density) { (cellSizePx * totalRows).toDp() }

                Canvas(
                    modifier = Modifier
                        .size(width = mazeWidthDp, height = mazeHeightDp)
                        .background(Color(0xFF0A1426), shape = RoundedCornerShape(8.dp))
                ) {
                    val inset = 1f
                    val cornerRadius = androidx.compose.ui.geometry.CornerRadius(cellSizePx * 0.18f)

                    for (r in 0 until totalRows) {
                        for (c in 0 until totalCols) {
                            val cell = grid[r][c]

                            val color = when (cell) {
                                is MazeCell.End -> Color(0xFF0F3D2C)
                                is MazeCell.Start -> Color(0xFF13335A)
                                is MazeCell.Trap -> Color(0xFF1A2A4A)
                                is MazeCell.Wall -> Color(0xFF060C1A)
                                is MazeCell.Path -> if (visitedCells.contains(Pair(r, c))) {
                                    Color(0xFF1C3258)
                                } else {
                                    Color(0xFF0F1D36)
                                }
                            }

                            drawRoundRect(
                                color = color,
                                topLeft = Offset(c * cellSizePx + inset, r * cellSizePx + inset),
                                size = Size(cellSizePx - inset * 2, cellSizePx - inset * 2),
                                cornerRadius = cornerRadius
                            )

                            if (cell is MazeCell.Trap) {
                                drawCircle(
                                    color = Color(0xFFD85A30),
                                    radius = cellSizePx * 0.16f,
                                    center = Offset(
                                        c * cellSizePx + cellSizePx / 2f,
                                        r * cellSizePx + cellSizePx / 2f
                                    )
                                )
                            }
                        }
                    }

                    val playerCenter = Offset(
                        playerCol * cellSizePx + cellSizePx / 2f,
                        playerRow * cellSizePx + cellSizePx / 2f
                    )
                    drawCircle(
                        color = Color(0xFFD4537E),
                        radius = cellSizePx * 0.42f,
                        center = playerCenter,
                        alpha = 0.25f
                    )
                    drawCircle(
                        color = Color(0xFFED93B1),
                        radius = cellSizePx * 0.26f,
                        center = playerCenter
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x1AFFFFFF), shape = RoundedCornerShape(24.dp))
                .padding(vertical = 14.dp)
        ) {
            NeonButton(text = "▲") { movePlayer(-1, 0) }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                NeonButton(text = "◀") { movePlayer(0, -1) }
                NeonButton(text = "▶") { movePlayer(0, 1) }
            }
            Spacer(modifier = Modifier.height(8.dp))
            NeonButton(text = "▼") { movePlayer(1, 0) }
        }
    }
}

@Composable
private fun HudStat(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0x1AFFFFFF), RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, color = Color(0x99FFFFFF), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = valueColor, fontSize = 17.sp, fontWeight = FontWeight.Medium)
    }
}