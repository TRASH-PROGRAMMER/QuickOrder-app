package com.example.quickorderapp.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quickorderapp.R

/**
 * Función componible que representa la pantalla de registro.
 * Incluye validaciones, accesibilidad y mejoras de usabilidad.
 */
@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    val email = emailInput.trim().lowercase()
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Validaciones
    val nameError = if (name.isNotBlank() && name.length < 3) "Nombre demasiado corto" else null
    
    val emailError = when {
        emailInput.isEmpty() -> null
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo inválido"
        else -> null
    }

    val passwordError = when {
        password.isEmpty() -> null
        password.length < 8 -> "Mínimo 8 caracteres"
        !password.any { it.isUpperCase() } -> "Falta una mayúscula"
        !password.any { it.isDigit() } -> "Falta un número"
        else -> null
    }

    val confirmPasswordError = when {
        confirmPassword.isEmpty() -> null
        confirmPassword != password -> "No coinciden"
        else -> null
    }

    val isFormValid = name.isNotBlank() && 
                     email.isNotBlank() && 
                     password.isNotBlank() && 
                     confirmPassword.isNotBlank() &&
                     nameError == null && 
                     emailError == null && 
                     passwordError == null && 
                     confirmPasswordError == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "REGISTRO",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )
        
        Spacer(Modifier.height(24.dp))

        // Campo Nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            isError = nameError != null,
            supportingText = { nameError?.let { Text(it) } },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(Modifier.height(8.dp))

        // Campo Email
        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Correo electrónico") },
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it) } },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(Modifier.height(8.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it) } },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                Text(
                    text = if (passwordVisible) "Ocultar" else "Mostrar",
                    color = colorResource(R.color.esmeralda),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { passwordVisible = !passwordVisible },
                    style = MaterialTheme.typography.labelMedium
                )
            }
        )

        Spacer(Modifier.height(8.dp))

        // Campo Confirmar Contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            isError = confirmPasswordError != null,
            supportingText = { confirmPasswordError?.let { Text(it) } },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                Text(
                    text = if (confirmPasswordVisible) "Ocultar" else "Mostrar",
                    color = colorResource(R.color.esmeralda),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { confirmPasswordVisible = !confirmPasswordVisible },
                    style = MaterialTheme.typography.labelMedium
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            enabled = isFormValid,
            onClick = {
                // Lógica de registro
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.esmeralda)
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(Modifier.height(16.dp))

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
