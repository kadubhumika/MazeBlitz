package com.example.mazeblitz.network

import com.example.mazeblitz.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("create-player")
    suspend fun createPlayer(
        @Body player: Player
    ): Response<Player>


    @POST("generate-maze")
    suspend fun generateMaze(
        @Body request: DifficultyRequest
    ): Response<MazeResponse>


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
}