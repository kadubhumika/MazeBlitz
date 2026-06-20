package com.example.mazeblitz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mazeblitz.viewmodel.MazeViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MazeViewModel
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val primaryBlue = Color(0xFF1E88E5)
    val softBlue = Color(0xFFE3F2FD)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF0D47A1),
        unfocusedTextColor = Color(0xFF0D47A1),
        focusedBorderColor = primaryBlue,
        unfocusedBorderColor = Color(0xFF90CAF9),
        cursorColor = primaryBlue,
        focusedLabelColor = primaryBlue,
        unfocusedLabelColor = Color(0xFF607D8B)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        softBlue,
                        Color.White
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "PLAYER PORTAL",
                color = primaryBlue,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            // Username
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    if (errorMessage.isNotEmpty()) errorMessage = ""
                },
                label = { Text("Username") },
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            // City
            OutlinedTextField(
                value = city,
                onValueChange = {
                    city = it
                    if (errorMessage.isNotEmpty()) errorMessage = ""
                },
                label = { Text("City (For Sign Up)") },
                colors = textFieldColors,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ===== BUTTON STYLE (REPLACED NEON) =====
            @Composable
            fun ActionButton(text: String, onClick: () -> Unit) {
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = text,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ActionButton("Login / Start Game") {
                    if (username.isBlank() || password.isBlank()) {
                        errorMessage = "Fields cannot be empty!"
                    } else {
                        viewModel.login(
                            username = username,
                            password = password,
                            onError = { backendError ->
                                errorMessage = backendError
                            },
                            onSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                }

                ActionButton("Sign Up As New Player") {
                    if (username.isBlank() || password.isBlank() || city.isBlank()) {
                        errorMessage = "Please enter Username, Password, AND City to Sign Up!"
                    } else {
                        viewModel.createPlayer(
                            username = username,
                            city = city,
                            password = password,
                            onSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}