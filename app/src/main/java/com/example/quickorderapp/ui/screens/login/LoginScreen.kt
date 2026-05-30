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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.example.quickorderapp.ui.theme.Typography
import androidx.compose.ui.res.colorResource
import com.example.quickorderapp.R


/**

 * Función componible que representa la pantalla de inicio de sesión de la aplicación.

 *
 * Esta pantalla proporciona campos para ingresar las credenciales del usuario (correo electrónico y contraseña) y

 * gestiona la navegación a la pantalla de inicio tras iniciar sesión o a la pantalla de registro.

 *
 * @param navController El [NavController] utilizado para gestionar la navegación de la aplicación entre pantallas.

 */
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") } // Cambia el tipo de dato a String
    var password by remember { mutableStateOf("") }// Cambia el tipo de dato a String
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),// Agrega padding
        verticalArrangement = Arrangement.Center// Centra verticalmente


    ) {
        Text("LOGIN",style = MaterialTheme.typography.headlineMedium, textAlign=TextAlign.Center , modifier = Modifier.fillMaxWidth(),fontWeight = FontWeight.Bold,
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)// Agrega padding
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)// Agrega padding

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
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.esmeralda)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally) // Agrega padding
        ) {
            Text("Login" ,style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No tienes una cuenta? Regístrate",
            color = colorResource(R.color.esmeralda) ,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                navController.navigate("register")
            }
        )
    }}
