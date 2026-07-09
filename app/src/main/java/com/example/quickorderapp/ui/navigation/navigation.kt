package com.example.quickorderapp.ui.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickorderapp.ui.screens.login.LoginScreen
import com.example.quickorderapp.ui.screens.login.RegisterScreen
import com.example.quickorderapp.ui.screens.products.ProductScreen
import com.example.quickorderapp.ui.screens.products.AddProductScreen
import com.example.quickorderapp.ui.screens.MainScreen
import com.example.quickorderapp.ui.screens.mesas.MesaScreen

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Configura el gráfico de navegación de la aplicación.
 */
@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = startDestination) {

        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("home") { MainScreen(navController) } // Redirigir a MainScreen si se intenta ir a home
        composable("ProductScreen") { ProductScreen() }
        composable("MesaScreen") { MesaScreen() }
        composable(
            route = "AddProductScreen?productId={productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: -1
            AddProductScreen(navController, productId)
        }
    }
}