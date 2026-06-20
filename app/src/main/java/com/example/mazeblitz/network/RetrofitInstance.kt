package com.example.mazeblitz.network

import com.example.mazeblitz.model.MazeCell
import com.example.mazeblitz.model.mazeCellDeserializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.11.244.136:8000/"

    // Without this registered adapter, Gson has no idea how to turn a JSON
    // number/string into the MazeCell sealed class, since it isn't a plain
    // data class. This was the cause of the maze not rendering: generateMaze()
    // would fail to parse the response body, mazeResponse.value stayed null,
    // and GameScreen's "maze?.maze_data?.maze?.let { ... }" block never ran -
    // while the HUD and D-pad buttons rendered fine since they don't depend
    // on maze being non-null.
    private val gson = GsonBuilder()
        .registerTypeAdapter(MazeCell::class.java, mazeCellDeserializer)
        .create()

    val api: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}