package com.example.financemanagementapp

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository, context: Context) : ViewModel() {

    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    var isAuthenticated by mutableStateOf(getAuthState())
        private set

    var currentUser by mutableStateOf<RegisterEntity?>(loadCurrentUser())
        private set

    var errorMessage by mutableStateOf<String?>(null)

    var isOperationInProgress by mutableStateOf(false)
        private set

    // Check if a user with the given username already exists
    fun checkUserExists(username: String) {
        viewModelScope.launch {
            val user = userRepository.getUserByUsername(username)
            errorMessage = if (user != null) "User already exists. Please Login!" else null
        }
    }

    // Simulated registration process
    suspend fun register(username: String, password: String, confirmPassword: String) {
        if (isOperationInProgress) return
        isOperationInProgress = true
        try {
            // Simulated registration
            if (password == confirmPassword && userRepository.getUserByUsername(username) == null) {
                userRepository.registerUser(RegisterEntity(username = username, password = password, confirmPassword = password))
            }
        } finally {
            isOperationInProgress = false
        }
    }

    // Register a new user
    fun registerUser(username: String, password: String) {
        if (isOperationInProgress) return
        isOperationInProgress = true
        viewModelScope.launch {
            try {
                val existingUser = userRepository.getUserByUsername(username)
                if (existingUser != null) {
                    errorMessage = "User already exists! Please Login"
                } else {
                    val newUser = RegisterEntity(username = username, password = password, confirmPassword = password)
                    userRepository.registerUser(newUser)
                    errorMessage = null
                    saveAuthState(true)
                    saveCurrentUser(newUser)
                    currentUser = newUser
                }
            } finally {
                isOperationInProgress = false
            }
        }
    }

    // Authenticate user with username and password
    fun authenticate(username: String, password: String) {
        if (isOperationInProgress) return
        isOperationInProgress = true
        viewModelScope.launch {
            try {
                val user = userRepository.authenticateUser(username, password)
                if (user != null) {
                    isAuthenticated = true
                    currentUser = user
                    errorMessage = null
                    saveAuthState(true)
                    saveCurrentUser(user)
                } else {
                    errorMessage = "Invalid username or password!"
                }
            } finally {
                isOperationInProgress = false
            }
        }
    }

    // Logout the current user
    fun logout() {
        isAuthenticated = false
        currentUser = null
        clearAuthState()
        clearCurrentUser()
    }

    // Get authentication state from SharedPreferences
    private fun getAuthState(): Boolean {
        return prefs.getBoolean("isAuthenticated", false)
    }

    // Save authentication state to SharedPreferences
    fun saveAuthState(authState: Boolean) {
        prefs.edit().putBoolean("isAuthenticated", authState).apply()
    }

    // Load current user from SharedPreferences
    private fun loadCurrentUser(): RegisterEntity? {
        val username = prefs.getString("username", null) ?: return null
        val password = prefs.getString("password", null) ?: return null
        return RegisterEntity(username = username, password = password, confirmPassword = password)
    }

    // Save current user to SharedPreferences
    private fun saveCurrentUser(user: RegisterEntity) {
        prefs.edit().putString("username", user.username)
            .putString("password", user.password)
            .apply()
    }

    // Clear authentication state from SharedPreferences
    private fun clearAuthState() {
        prefs.edit().remove("isAuthenticated").apply()
    }

    // Clear current user from SharedPreferences
    private fun clearCurrentUser() {
        prefs.edit().remove("username")
            .remove("password")
            .apply()
    }
}
