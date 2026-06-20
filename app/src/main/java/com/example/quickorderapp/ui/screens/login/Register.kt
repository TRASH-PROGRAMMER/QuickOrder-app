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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quickorderapp.R
import com.example.quickorderapp.domain.model.User
import com.example.quickorderapp.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    RegisterScreenContent(
        onRegister = { newUser ->
            viewModel.register(newUser) {
                navController.popBackStack()
            }
        },
        onLoginClick = { navController.popBackStack() }
    )
}

@Composable
fun RegisterScreenContent(
    onRegister: (User) -> Unit,
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    val email = emailInput.trim().lowercase()
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("MESERO") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Validaciones amigables
    val nameError = if (name.isNotEmpty() && name.length < 3) "Tu nombre debe tener al menos 3 letras" else null
    val emailError = if (emailInput.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Ese correo no parece correcto" else null
    val passwordError = if (password.isNotEmpty() && password.length < 8) "Usa al menos 8 caracteres" else null
    val confirmPasswordError = if (confirmPassword.isNotEmpty() && confirmPassword != password) "¡Ups! Las contraseñas no coinciden" else null

    val isFormValid = name.length >= 3 && 
                     android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && 
                     password.length >= 8 && 
                     confirmPassword == password

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Crea tu cuenta!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Únete a la familia QuickOrder", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(32.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("¿Cómo te llamas?") },
                    isError = nameError != null,
                    supportingText = { nameError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Tu correo electrónico") },
                    isError = emailError != null,
                    supportingText = { emailError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Text("Selecciona tu rol:", style = MaterialTheme.typography.labelLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedRole == "MESERO", onClick = { selectedRole = "MESERO" })
                    Text("Mesero", modifier = Modifier.clickable { selectedRole = "MESERO" })
                    Spacer(Modifier.width(16.dp))
                    RadioButton(selected = selectedRole == "ADMIN", onClick = { selectedRole = "ADMIN" })
                    Text("Administrador", modifier = Modifier.clickable { selectedRole = "ADMIN" })
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Crea una contraseña") },
                    isError = passwordError != null,
                    supportingText = { passwordError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "Ocultar" else "Mostrar", color = colorResource(R.color.esmeralda))
                        }
                    }
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirma tu contraseña") },
                    isError = confirmPasswordError != null,
                    supportingText = { confirmPasswordError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Text(if (confirmPasswordVisible) "Ocultar" else "Mostrar", color = colorResource(R.color.esmeralda))
                        }
                    }
                )

                Button(
                    enabled = isFormValid,
                    onClick = {
                        onRegister(User(nombre = name, correo = email, rol = selectedRole, password = password))
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.esmeralda)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("¡Listo, registrarme!", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        TextButton(onClick = onLoginClick) {
            Text("¿Ya tienes cuenta? Inicia sesión aquí", color = colorResource(R.color.esmeralda))
        }
    }
}
