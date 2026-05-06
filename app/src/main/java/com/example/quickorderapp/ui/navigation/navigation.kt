package com.example.quickorderapp.ui.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickorderapp.ui.screens.login.LoginScreen
import com.example.quickorderapp.ui.screens.home.HomeScreen
import com.example.quickorderapp.ui.screens.login.RegisterScreen



/**

 * Configura el gráfico de navegación de la aplicación.

 *
 * Este componente inicializa el [navController] y define el [NavHost],

 * asignando las rutas a sus respectivos componentes de pantalla, como Iniciar sesión,

 * Registrarse y Página de inicio.

 *
 * @param startDestination La ruta de la pantalla que se mostrará al iniciar la aplicación.

 */
@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = startDestination) {

        composable("login") { LoginScreen(navController,) }
        composable("register") {RegisterScreen (navController) }
        composable("home") { HomeScreen(navController) }
    }
}