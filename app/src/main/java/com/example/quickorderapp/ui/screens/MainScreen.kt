package com.example.quickorderapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quickorderapp.ui.screens.home.HomeScreen
import com.example.quickorderapp.ui.screens.order.OrderScreen
import com.example.quickorderapp.ui.screens.profile.ProfileScreen
import com.example.quickorderapp.viewmodel.HomeViewModel

@Composable
fun MainScreen(
    rootNavController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val userRole by homeViewModel.userRole.collectAsStateWithLifecycle()

    // Todos los usuarios usan ahora el ClientMainScreen para tener acceso al Perfil y Navegación
    ClientMainScreen(rootNavController, userRole)
}

@Composable
fun ClientMainScreen(rootNavController: NavController, role: String) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Inicio,
        Screen.Pedido,
        Screen.Perfil
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Inicio.route) {
                HomeScreen(rootNavController)
            }
            composable(Screen.Pedido.route) {
                OrderScreen()
            }
            composable(Screen.Perfil.route) {
                ProfileScreen()
            }
        }
    }
}


sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Inicio : Screen("inicio", "Inicio", Icons.Default.Home)
    object Pedido : Screen("pedido", "Pedido", Icons.Default.ShoppingCart)
    object Perfil : Screen("perfil", "Mi Perfil", Icons.Default.Person)
}
