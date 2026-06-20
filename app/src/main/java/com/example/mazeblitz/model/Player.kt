package com.example.mazeblitz.model

data class Player(
    val username: String,
    val city: String,
    val password: String,

    val player_id: Int? = null,

    val easy_level_completed: Int? = 0,

    val medium_level_completed: Int? = 0,

    val hard_level_completed: Int? = 0,

    val total_score: Int? = 0
)