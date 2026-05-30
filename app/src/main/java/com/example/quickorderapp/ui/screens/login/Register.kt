package com.example.quickorderapp.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quickorderapp.R

/**

 * Función componible que representa la pantalla de registro.

 *
 * Esta pantalla proporciona un formulario para que los nuevos usuarios creen una cuenta ingresando su
 * nombre, correo electrónico y contraseña. Incluye una validación básica para asegurar que las contraseñas coincidan
 * antes de continuar con la lógica de registro.

 *
 * @param navController El [NavController] utilizado para navegar entre pantallas y manejar las operaciones de la pila de retroceso.

 */
@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("REGISTRO", style = MaterialTheme.typography.headlineMedium, textAlign=TextAlign.Center , modifier = Modifier.fillMaxWidth(),fontWeight = FontWeight.Bold,)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (password == confirmPassword) {
                    println("Registro exitoso")
                    navController.popBackStack()
                } else {
                    println("Las contraseñas no coinciden")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.esmeralda)
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Registrar", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            color = colorResource(R.color.esmeralda),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                navController.popBackStack()
            }
        )
    }
}
