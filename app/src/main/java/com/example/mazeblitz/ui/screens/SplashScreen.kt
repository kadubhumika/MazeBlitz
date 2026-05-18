package com.example.mazeblitz.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    // Smooth fade-in animation state
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Run animation concurrently with the splash delay
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )
        delay(1300) // Total delay matches your original 2500ms

        // FIXED NAVIGATION: Pops the splash screen from the backstack
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0010), // Ultra-dark magenta/wine
                        Color(0xFF0D001A), // Deep dark violet
                        Color(0xFF050505)  // Near absolute black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Main container applies the alpha animation smoothly to all child texts
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .alpha(alpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MazeBlitz",
                color = Color(0xFFFF007F), // Vibrant, modern cyber-pink
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Think Fast. Escape Faster.",
                color = Color.White.copy(alpha = 0.6f), // Faint, clean contrast
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )
        }
    }
}
