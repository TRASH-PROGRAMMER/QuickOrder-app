package com.example.quickorderapp.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quickorderapp.R
import com.example.quickorderapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var emailInput by remember { mutableStateOf("") }
    val email = emailInput.trim().lowercase()
    var password by remember { mutableStateOf("") }
    
    val emailError = when {
        email.isBlank() -> "El correo es obligatorio"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Ingrese un correo válido"
        else -> null
    }
    
    val passwordError = when {
        password.isBlank() -> "La contraseña es obligatoria"
        password.length < 4 -> "Mínimo 4 caracteres"
        else -> null
    }
    
    val isFormValid = emailError == null && passwordError == null && email.isNotBlank() && password.isNotBlank()
    
    var passwordVisible by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LOGIN",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Correo electrónico") },
            isError = emailError != null,
            supportingText = { emailError?.let { Text(it) } },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            isError = passwordError != null,
            supportingText = { passwordError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
        
        Spacer(Modifier.height(24.dp))
        
        Button(
            enabled = isFormValid,
            onClick = {
                viewModel.login(email)
                navController.navigate("home")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.esmeralda)
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.bodyLarge)
        }
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            text = "¿No tienes cuenta? Regístrate",
            color = colorResource(R.color.esmeralda),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("register")
                }
        )
    }
}
