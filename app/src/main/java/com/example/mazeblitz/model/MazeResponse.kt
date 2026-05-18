package com.example.mazeblitz.model

data class DifficultyRequest(
    val difficulty: String,
    val level: Int
)


data class MazeResponse(
    val difficulty: String,
    val level: Int,
    val timer: Int,
    val maze_size: String,

    val maze_data: MazeData
)


data class MazeData(
    val maze: List<List<Any>>,
    val start: List<Int>,
    val end: List<Int>
)