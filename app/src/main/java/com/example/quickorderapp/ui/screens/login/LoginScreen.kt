package com.example.quickorderapp.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp



@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") } // Cambia el tipo de dato a String
    var password by remember { mutableStateOf("") }// Cambia el tipo de dato a String
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),// Agrega padding
        verticalArrangement = Arrangement.Center// Centra verticalmente


    ) {
     Text("LOGIN",style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.padding(bottom = 16.dp)// Agrega padding
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.padding(bottom = 16.dp)// Agrega padding
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                 if (email=="test@test.com" && password=="1234")
                 {
                     println("Login exitoso")
                 }
                navController.navigate("home")
            },
            modifier = Modifier.padding(bottom = 16.dp)// Agrega padding
        ) {
            Text("Login" ,style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No tienes una cuenta? Regístrate",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate("register")
            }
        )
}}
