package com.example.mazeblitz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazeblitz.model.*
import com.example.mazeblitz.network.RetrofitInstance
import kotlinx.coroutines.launch

class MazeViewModel : ViewModel() {

    var mazeResponse = mutableStateOf<MazeResponse?>(null)

    var playerId = mutableStateOf(0)

    var playerName = mutableStateOf("")
    var leaderboard =
        mutableStateOf<List<LeaderboardItem>>(emptyList())

    var history = mutableStateOf<List<Score>>(emptyList())


    fun createPlayer(
        username: String,
        city: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            try {

                val response = RetrofitInstance.api.createPlayer(
                    Player(username, city)
                )

                if (response.isSuccessful) {

                    playerName.value = username
                    playerId.value = 1

                    onSuccess()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } }
    }


    fun generateMaze(
        difficulty: String,
        level: Int
    ) {

        viewModelScope.launch {

            try {

                val response = RetrofitInstance.api.generateMaze(
                    DifficultyRequest(difficulty, level)
                )

                if (response.isSuccessful) {

                    mazeResponse.value = response.body()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun submitScore(
        difficulty: String,
        level: Int,
        score: Int,
        time: Float,
        steps: Int
    ) {viewModelScope.launch {

        try {

            RetrofitInstance.api.submitScore(
                Score(
                    player_id = playerId.value,
                    score = score,
                    difficulty = difficulty,
                    time_taken = time,
                    steps = steps,
                    level = level
                )
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    }
    fun fetchLeaderboard() {

        viewModelScope.launch {

            try {

                val response =
                    RetrofitInstance.api.getLeaderboard()

                if (response.isSuccessful) {

                    leaderboard.value =
                        response.body() ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun fetchHistory() {

        viewModelScope.launch {

            try {

                val response = RetrofitInstance.api.getHistory(
                    playerId.value
                )

                if (response.isSuccessful) {

                    history.value = response.body() ?: emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}