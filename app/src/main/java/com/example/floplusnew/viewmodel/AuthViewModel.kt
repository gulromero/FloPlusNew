package com.example.floplusnew.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                _isLoggedIn.value = true
                _loginError.value = null // clear error message hvis login er successed
            } catch (e: Exception) {
                _isLoggedIn.value = false
                _loginError.value = e.message // firebase default error message
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _isLoggedIn.value = true
                _loginError.value = null
            } catch (e: Exception) {
                _isLoggedIn.value = false
                _loginError.value = e.message
            }
        }
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
    }

    fun getUserId(): String? = auth.currentUser?.uid
}