package com.example.quickorderapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.quickorderapp.R
import com.example.quickorderapp.ui.screens.admin.AdminDashboardScreen
import com.example.quickorderapp.ui.screens.admin.AdminProductListScreen
import com.example.quickorderapp.ui.screens.home.AboutUsConsumerScreen
import com.example.quickorderapp.ui.screens.home.DailyMessageConsumerScreen
import com.example.quickorderapp.ui.screens.home.HomeScreen
import com.example.quickorderapp.ui.screens.order.OrderDetailScreen
import com.example.quickorderapp.ui.screens.order.OrderHistoryScreen
import com.example.quickorderapp.ui.screens.profile.ProfileScreen
import com.example.quickorderapp.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

import com.example.quickorderapp.ui.screens.mesero.MeseroDashboardScreen

@Composable
fun MainScreen(
    rootNavController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val userRole by homeViewModel.userRole.collectAsStateWithLifecycle()

    when (userRole) {
        "ADMIN" -> AdminMainScreen(rootNavController)
        "MESERO" -> MeseroDashboardScreen(rootNavController)
        else -> ClientMainScreen(rootNavController, homeViewModel)
    }
}

@Composable
fun AdminMainScreen(rootNavController: NavController) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Inicio,
        Screen.Productos,
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
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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
            composable(Screen.Inicio.route) { AdminDashboardScreen(rootNavController) }
            composable(Screen.Productos.route) { AdminProductListScreen(rootNavController) }
            composable(Screen.Perfil.route) { ProfileScreen(navController = rootNavController) }
        }
    }
}

@Composable
fun ClientMainScreen(
    rootNavController: NavController, 
    homeViewModel: HomeViewModel
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomItems = listOf(
        Screen.Inicio,
        Screen.Pedido,
        Screen.Perfil
    )

    val drawerItems = listOf(
        DrawerItem("Mensaje del Día", Icons.Default.Announcement, "daily_msg"),
        DrawerItem("Sobre Nosotros", Icons.Default.Info, "about"),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text("QuickOrder", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium, color = colorResource(R.color.esmeralda), fontWeight = FontWeight.Bold)
                Divider()
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                Spacer(Modifier.weight(1f))
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        homeViewModel.logout {
                            rootNavController.navigate("login") { popUpTo(0) { inclusive = true } }
                        }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomItems.forEach { screen ->
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
                    HomeScreen(
                        navController = rootNavController,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable(Screen.Pedido.route) {
                    OrderHistoryScreen(
                        navController = navController, // Use inner for history->detail
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable(Screen.Perfil.route) {
                    ProfileScreen(
                        navController = rootNavController,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable("daily_msg") {
                    DailyMessageConsumerScreen(
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable("about") {
                    AboutUsConsumerScreen(
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable(
                    route = "order_detail/{orderId}",
                    arguments = listOf(navArgument("orderId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getInt("orderId") ?: -1
                    OrderDetailScreen(orderId, navController)
                }
            }
        }
    }
}

data class DrawerItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val route: String)

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Inicio : Screen("inicio", "Inicio", Icons.Default.Home)
    object Productos : Screen("productos", "Productos", Icons.Default.RestaurantMenu)
    object Pedido : Screen("pedido", "Pedido", Icons.Default.ShoppingCart)
    object Perfil : Screen("perfil", "Mi Perfil", Icons.Default.Person)
}
