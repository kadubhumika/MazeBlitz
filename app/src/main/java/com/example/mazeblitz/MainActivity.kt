package com.example.mazeblitz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mazeblitz.navigation.AppNavigation
import com.example.mazeblitz.ui.theme.MazeBlitzTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MazeBlitzTheme {

                AppNavigation()
            }
        }
    }
}