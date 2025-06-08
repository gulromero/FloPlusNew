package com.example.floplusnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.floplusnew.ui.screens.*
import com.example.floplusnew.ui.theme.FloPlusNewTheme
import com.example.floplusnew.viewmodel.AuthViewModel
import com.example.floplusnew.viewmodel.CycleViewModel
import com.example.floplusnew.data.CycleStartDateManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FloPlusNewTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val authViewModel = remember { AuthViewModel() }
                val cycleManager = remember { CycleStartDateManager(context.applicationContext) }
                val cycleViewModel = remember { CycleViewModel(cycleManager) }

                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn) "cycle" else "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { navController.navigate("cycle") { popUpTo("login") { inclusive = true } } },
                            onSignupClick = { navController.navigate("signup") },
                            viewModel = authViewModel
                        )
                    }
                    composable("signup") {
                        SignupScreen(
                            onSignupSuccess = { navController.navigate("cycle") { popUpTo("signup") { inclusive = true } } },
                            onLoginClick = { navController.navigate("login") },
                            viewModel = authViewModel
                        )
                    }
                    composable("cycle") {
                        CycleScreen(
                            viewModel = cycleViewModel,
                            authViewModel = authViewModel,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("cycle") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
