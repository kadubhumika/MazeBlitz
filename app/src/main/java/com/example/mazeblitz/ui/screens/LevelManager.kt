package com.example.mazeblitz.ui.screens

import android.content.Context
import com.example.mazeblitz.model.UserSession

object LevelManager {

    private const val PREF_NAME = "maze_progress"

    fun isLevelCompleted(
        context: Context,
        difficulty: String,
        level: Int
    ): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("${UserSession.playerId}_${difficulty}_$level", false)
    }

    fun markLevelCompleted(
        context: Context,
        difficulty: String,
        level: Int
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("${UserSession.playerId}_${difficulty}_$level", true)
            .apply()

        unlockLevel(context, difficulty, level + 1)
    }

    fun unlockLevel(context: Context, difficulty: String, level: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val current = getUnlockedLevel(context, difficulty)
        if (level > current) {
            prefs.edit()
                .putInt("${UserSession.playerId}_$difficulty", level)
                .apply()
        }
    }

    fun getUnlockedLevel(context: Context, difficulty: String): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt("${UserSession.playerId}_$difficulty", 1)
    }

    fun isLevelUnlocked(
        context: Context,
        difficulty: String,
        level: Int
    ): Boolean {
        return level <= getUnlockedLevel(context, difficulty)
    }
}