package com.example.quickorderapp.ui.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickorderapp.ui.screens.login.LoginScreen
import com.example.quickorderapp.ui.screens.home.HomeScreen
import com.example.quickorderapp.ui.screens.login.RegisterScreen



@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = startDestination) {

        composable("login") { LoginScreen(navController,) }
        composable("register") {RegisterScreen (navController) }
        composable("home") { HomeScreen(navController) }
    }
}