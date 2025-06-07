package com.example.floplusnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.floplusnew.data.CycleStartDateManager
import com.example.floplusnew.ui.screens.AuthScreen
import com.example.floplusnew.ui.screens.CycleScreen
import com.example.floplusnew.ui.theme.FloPlusNewTheme
import com.example.floplusnew.viewmodel.AuthViewModel
import com.example.floplusnew.viewmodel.CycleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FloPlusNewTheme {
                val context = LocalContext.current
                val authViewModel = remember { AuthViewModel() }
                var isAuthenticated by remember { mutableStateOf(authViewModel.isLoggedIn.value) }

                LaunchedEffect(authViewModel.isLoggedIn) {
                    authViewModel.isLoggedIn.collect { isLoggedIn ->
                        isAuthenticated = isLoggedIn
                    }
                }

                if (isAuthenticated) {
                    val cycleManager = remember { CycleStartDateManager(context.applicationContext) }
                    val viewModel = remember { CycleViewModel(cycleManager) }
                    CycleScreen(viewModel = viewModel)
                } else {
                    AuthScreen(
                        viewModel = authViewModel,
                        onAuthSuccess = {
                            isAuthenticated = true
                        }
                    )
                }
            }
        }

    }
}
