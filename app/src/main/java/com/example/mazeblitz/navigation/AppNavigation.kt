package com.example.mazeblitz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mazeblitz.ui.screens.*

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }
        composable("levels/{difficulty}") {

            val difficulty =
                it.arguments?.getString("difficulty") ?: "easy"

            LevelScreen(
                navController,
                difficulty
            )
        }
        composable("home") {
            HomeScreen(navController)
        }

        composable("difficulty") {
            DifficultyScreen(navController)
        }


        composable("game/{difficulty}/{level}") {

            val difficulty = it.arguments?.getString("difficulty") ?: "easy"
            val level = it.arguments?.getString("level")?.toInt() ?: 1

            GameScreen(
                navController,
                difficulty,
                level
            )
        }

        composable("leaderboard") {
            LeaderboardScreen(navController)
        }

        composable("history") {
            HistoryScreen(navController)
        }
        composable(
            "result/{difficulty}/{level}/{steps}"
        ) {

            val difficulty =
                it.arguments?.getString("difficulty") ?: "easy"

            val level =
                it.arguments?.getString("level")?.toInt() ?: 1

            val steps =
                it.arguments?.getString("steps")?.toInt() ?: 0

            ResultScreen(
                navController,
                difficulty,
                level,
                steps
            )
        }
    }
    }