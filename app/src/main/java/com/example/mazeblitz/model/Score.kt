package com.example.mazeblitz.model

data class Score(
    val player_id: Int,
    val score: Int,
    val difficulty: String,
    val time_taken: Float,
    val steps: Int,
    val level: Int
)
data class LeaderboardItem(

    val username: String,

    val score: Int
)