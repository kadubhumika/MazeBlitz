package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mazeblitz.ui.components.NeonButton
import com.example.mazeblitz.viewmodel.MazeViewModel

@Composable
fun LoginScreen(
    navController: NavController
) {
    val viewModel: MazeViewModel = viewModel()

    var username by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Reusable styling for text field inputs
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color(0xFFFF007F), // Neon pink highlight when active
        unfocusedLabelColor = Color.White.copy(alpha = 0.6f), // Clean white-grey when idle
        focusedBorderColor = Color(0xFFFF007F),
        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
        cursorColor = Color(0xFFFF007F)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0010), // Cyber-wine
                        Color(0xFF0D001A), // Deep dark violet
                        Color(0xFF050505)  // Near absolute black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Heading aligned cleanly at the top of the stack
            Text(
                text = "PLAYER LOGIN",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 2.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            // Username input field
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    if (errorMessage.isNotEmpty()) errorMessage = ""
                },
                label = { Text("Username", fontWeight = FontWeight.Medium) },
                colors = textFieldColors,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // City input field
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                    if (errorMessage.isNotEmpty()) errorMessage = ""
                },
                label = { Text("City", fontWeight = FontWeight.Medium) },
                colors = textFieldColors,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Dynamic error validation readout
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action button explicitly bound to fill configuration width
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                NeonButton(text = "Start Game") {
                    if (username.isBlank() || city.isBlank()) {
                        errorMessage = "Fields cannot be empty!"
                    } else {
                        viewModel.createPlayer(username.trim(), city.trim()) {
                            navController.navigate("home") {
                                // Wipe login off stack history
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }
}
