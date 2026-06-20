package com.example.mazeblitz.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mazeblitz.model.*
import com.example.mazeblitz.network.RetrofitInstance
import kotlinx.coroutines.launch

class MazeViewModel : ViewModel() {
    var playerProgress = mutableStateOf<PlayerProgress?>(null)
    var mazeResponse = mutableStateOf<MazeResponse?>(null)
    var playerName = mutableStateOf("")
    var leaderboard = mutableStateOf<List<LeaderboardItem>>(emptyList())
    var history = mutableStateOf<List<Score>>(emptyList())

    fun createPlayer(
        username: String,
        city: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createPlayer(
                    Player(username, city, password)
                )

                if (response.isSuccessful) {
                    response.body()?.let { player ->
                        playerName.value = player.username
                        UserSession.playerId = player.player_id ?: -1
                        UserSession.username = player.username
                        Log.d("MazeDebug", "Player created. ID = ${UserSession.playerId}")
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun login(
        username: String,
        password: String,
        onError: (String) -> Unit,
        onSuccess: (LoginResponse) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(username, password))

                if (response.isSuccessful && response.body() != null) {
                    val player = response.body()!!
                    UserSession.playerId = player.player_id ?: -1
                    UserSession.username = player.username ?: ""
                    Log.d("MazeDebug", "LOGIN OK ID = ${UserSession.playerId}")
                    onSuccess(player)
                } else {
                    onError("User not found or wrong password! If you are new, click Sign Up.")
                }
            } catch (e: Exception) {
                onError("Network error. Please check your connection.")
                e.printStackTrace()
            }
        }
    }

    fun deleteHistory() {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.deleteHistory(UserSession.playerId)
                history.value = emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetSession() {
        mazeResponse.value = null
        leaderboard.value = emptyList()
        history.value = emptyList()
        playerProgress.value = null
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
                } else {
                    Log.e("MazeDebug", "generateMaze failed: ${response.code()} ${response.message()}")
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
        steps: Int,
        onError: (() -> Unit)? = null
    ) {
        Log.d("MazeDebug", "Submitting score for playerId = ${UserSession.playerId}")
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.submitScore(
                    Score(
                        player_id = UserSession.playerId,
                        score = score,
                        difficulty = difficulty,
                        time_taken = time,
                        steps = steps,
                        level = level
                    )
                )

                if (!response.isSuccessful) {
                    Log.e("MazeDebug", "submitScore failed: ${response.code()} ${response.message()}")
                    onError?.invoke()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError?.invoke()
            }
        }
    }

    fun fetchPlayerProgress() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPlayerProgress(UserSession.playerId)
                if (response.isSuccessful) {
                    playerProgress.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchLeaderboard() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getLeaderboard()
                if (response.isSuccessful) {
                    leaderboard.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchHistory() {
        Log.d("MazeDebug", "Fetching history for playerId = ${UserSession.playerId}")
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getHistory(UserSession.playerId)
                if (response.isSuccessful) {
                    history.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}