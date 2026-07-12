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

import com.example.quickorderapp.ui.screens.admin.CategoryManagementScreen
import com.example.quickorderapp.ui.screens.admin.DailyMessageScreen
import com.example.quickorderapp.ui.screens.admin.AboutUsScreen
import com.example.quickorderapp.ui.screens.admin.StatsScreen

import com.example.quickorderapp.ui.screens.profile.ProfileScreen
import com.example.quickorderapp.ui.screens.order.CartScreen
import com.example.quickorderapp.ui.screens.order.OrderDetailScreen

import com.example.quickorderapp.ui.screens.mesero.MeseroOrderDetailScreen

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
        composable("home") { MainScreen(navController) }
        composable("ProductScreen") { ProductScreen() }
        composable("MesaScreen") { MesaScreen() }
        composable("category_mgmt") { CategoryManagementScreen() }
        composable("daily_message") { DailyMessageScreen() }
        composable("about_us") { AboutUsScreen() }
        composable("stats") { StatsScreen() }
        composable("perfil") { ProfileScreen() }
        composable("cart") { CartScreen(navController) }
        composable(
            route = "order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: -1
            OrderDetailScreen(orderId, navController)
        }
        composable(
            route = "mesero_order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: -1
            MeseroOrderDetailScreen(orderId, navController)
        }
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