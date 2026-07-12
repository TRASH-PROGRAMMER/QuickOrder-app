package com.example.quickorderapp.ui.screens.mesero

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quickorderapp.R
import com.example.quickorderapp.ui.screens.home.AboutUsConsumerScreen
import com.example.quickorderapp.ui.screens.home.DailyMessageConsumerScreen
import com.example.quickorderapp.ui.screens.home.HomeScreen
import com.example.quickorderapp.ui.screens.profile.ProfileScreen
import com.example.quickorderapp.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun MeseroDashboardScreen(
    rootNavController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomItems = listOf(
        MeseroScreen("inicio", "Inicio", Icons.Default.Home),
        MeseroScreen("pedidos", "Pedidos", Icons.Default.Assignment),
        MeseroScreen("perfil", "Mi Perfil", Icons.Default.Person)
    )

    val drawerItems = listOf(
        MeseroScreen("daily_msg", "Mensaje del Día", Icons.Default.Announcement),
        MeseroScreen("about", "Sobre Nosotros", Icons.Default.Info)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text("QuickOrder - Mesero", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium, color = colorResource(R.color.esmeralda), fontWeight = FontWeight.Bold)
                HorizontalDivider()
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
                startDestination = "inicio",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("inicio") {
                    HomeScreen(
                        navController = rootNavController,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable("pedidos") {
                    MeseroOrdersScreen(
                        onOpenDrawer = { scope.launch { drawerState.open() } },
                        onOrderDetail = { orderId -> rootNavController.navigate("mesero_order_detail/$orderId") }
                    )
                }
                composable("perfil") {
                    ProfileScreen(
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
            }
        }
    }
}

data class MeseroScreen(val route: String, val title: String, val icon: ImageVector)
