package com.example.floplusnew.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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
                _loginError.value = null
            } catch (e: Exception) {
                _isLoggedIn.value = false

                val customMessage = when (e) {
                    is FirebaseAuthInvalidUserException -> "No account found with this email"
                    is FirebaseAuthInvalidCredentialsException -> "That password isn't correct"
                    else -> e.localizedMessage ?: "Something went wrong"
                }

                _loginError.value = customMessage
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

                val customMessage = when (e) {
                    is FirebaseAuthUserCollisionException -> "Someone’s already using that email"
                    is FirebaseAuthInvalidCredentialsException -> "The email address is badly formatted"
                    else -> e.localizedMessage ?: "Something went wrong during sign up"
                }
                _loginError.value = customMessage
            }
        }
    }


    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
    }

    fun getUserId(): String? = auth.currentUser?.uid
}