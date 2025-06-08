package com.example.floplusnew

import com.example.floplusnew.viewmodel.AuthViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    @Before
    fun setup() {
        viewModel = AuthViewModel()
    }

    @Test
    fun testLogout_setsIsLoggedInFalse() {
        viewModel.logout()
        assertFalse(viewModel.isLoggedIn.value)
    }

    @Test
    fun testLogin_withInvalidCredentials_setsIsLoggedInFalse() = runTest {
        viewModel.login("wrong@email.com", "wrongpass")
        Thread.sleep(2000)
        assertFalse(viewModel.isLoggedIn.value)
    }
}
