package com.example.financemanagementapp

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    var isAuthenticated: Boolean = false
        get() = auth.currentUser != null

    var errorMessage: String? = null
    fun logout() {
        auth.signOut()
        isAuthenticated=false
        saveAuthState(false) // Clear saved auth state
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("isFirstLaunch", true).apply()
    }

    fun registerUser(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = task.exception?.message
                    onError(errorMessage ?: "Registration failed")
                }
            }
    }

    fun loginUser(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("isFirstLaunch", false).apply()
                } else {
                    errorMessage = task.exception?.message
                    onError(errorMessage ?: "Login failed")
                }
            }
    }

    fun checkUserExists(username: String, onExists: () -> Unit, onNotExists: () -> Unit) {
        val userDocRef = firestore.collection("users").document(username)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    errorMessage = "User already exists!"
                    onExists()
                } else {
                    onNotExists()
                }
            }
            .addOnFailureListener {
                errorMessage = it.message
                onNotExists()
            }
    }

    fun saveAuthState(isAuthenticated: Boolean) {
        // Example: Save authentication state in SharedPreferences
        val sharedPref = context.getSharedPreferences("auth_state", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("is_authenticated", isAuthenticated)
            apply()
        }
    }

    fun authenticate(username: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = task.exception?.message
                    onError(errorMessage ?: "Login failed")
                }
            }
    }

}
