package com.example.mazeblitz.network

import com.example.mazeblitz.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("create-player")
    suspend fun createPlayer(
        @Body player: Player
    ): Response<Player>

    @GET("player-progress/{id}")
    suspend fun getPlayerProgress(
        @Path("id") id: Int
    ): Response<PlayerProgress>

    @POST("generate-maze")
    suspend fun generateMaze(
        @Body request: DifficultyRequest
    ): Response<MazeResponse>

    @POST("login")
    suspend fun login(
        @Body player: LoginRequest
    ): Response<LoginResponse>

    @POST("submit-score")
    suspend fun submitScore(
        @Body score: Score
    ): Response<Map<String, String>>

    @GET("leaderboard")
    suspend fun getLeaderboard(): Response<List<LeaderboardItem>>

    @GET("player-history/{id}")
    suspend fun getHistory(
        @Path("id") id: Int
    ): Response<List<Score>>

    @DELETE("player-history/{player_id}")
    suspend fun deleteHistory(
        @Path("player_id") playerId: Int
    ): Response<Unit>
}