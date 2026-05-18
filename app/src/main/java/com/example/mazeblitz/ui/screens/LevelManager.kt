package com.example.mazeblitz.ui.screens


import android.content.Context

object LevelManager {

    private const val PREF_NAME = "maze_progress"

    fun unlockLevel(
        context: Context,
        difficulty: String,
        level: Int
    ) {

        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        prefs.edit()
            .putInt(difficulty, level)
            .apply()
    }

    fun getUnlockedLevel(
        context: Context,
        difficulty: String
    ): Int {

        val prefs = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        return prefs.getInt(difficulty, 1)
    }

    fun isLevelUnlocked(
        context: Context,
        difficulty: String,
        level: Int
    ): Boolean {

        return level <= getUnlockedLevel(
            context,
            difficulty
        )
    }
}