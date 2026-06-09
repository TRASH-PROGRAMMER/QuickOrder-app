package com.example.quickorderapp.ui.screens.login

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quickorderapp.R
import com.example.quickorderapp.viewmodel.LoginUiState
import com.example.quickorderapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var emailInput by remember { mutableStateOf("") }
    val email = emailInput.trim().lowercase()
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Validaciones con mensajes amigables
    val emailError = when {
        emailInput.isEmpty() -> null
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Ese correo no parece válido, ¡revísalo!"
        else -> null
    }

    val passwordError = when {
        password.isEmpty() -> null
        password.length < 8 -> "Para tu seguridad, usa al menos 8 caracteres"
        !password.any { it.isUpperCase() } -> "¡No olvides incluir una mayúscula!"
        !password.any { it.isDigit() } -> "Añade al menos un número"
        else -> null
    }

    val isFormValid = emailError == null && passwordError == null && email.isNotBlank() && password.length >= 8

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "QuickOrder Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Hola de nuevo!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Ingresa para gestionar tus pedidos",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (uiState is LoginUiState.Error) {
            Text(
                text = (uiState as LoginUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Tu contraseña") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = passwordError != null,
                    supportingText = { passwordError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                text = if (passwordVisible) "Ocultar" else "Mostrar",
                                color = colorResource(R.color.esmeralda)
                            )
                        }
                    }
                )

                Button(
                    enabled = isFormValid && uiState !is LoginUiState.Loading,
                    onClick = {
                        viewModel.login(email, password)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.esmeralda)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState is LoginUiState.Loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text("¡Entrar!", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = { 
                viewModel.resetState()
                navController.navigate("register") 
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(colorResource(R.color.esmeralda))
            )
        ) {
            Text(
                "Quiero crear una cuenta",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.esmeralda)
            )
        }
    }
}
