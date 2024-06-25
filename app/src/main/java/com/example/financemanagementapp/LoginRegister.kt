
package com.example.financemanagementapp
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    authViewModel: AuthViewModel,
    navController: NavController // Inject NavController from your navigation component
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage = authViewModel.errorMessage

    LaunchedEffect(authViewModel.isAuthenticated) {
        if (authViewModel.isAuthenticated) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login Screen", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let { message ->
            Text(message, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Button(onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    onLoginSuccess(username, password)
                }
            }) {
                Text("Login")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = onNavigateToRegister) {
                Text("Register")
            }
        }
    }
}




@Composable
fun RegistrationScreen(
    onRegisterSuccess: () -> Unit,
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val errorMessage = authViewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registration Screen", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(16.dp))

        // Display error message if not null
        errorMessage?.let { message ->
            Text(message, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Check if passwords match
            if (password != confirmPassword) {
                authViewModel.errorMessage = "Passwords do not match!"
            }

            // Check if username and password fields are not empty
            else if (username.isNotEmpty() && password.isNotEmpty()) {
                // Check if user already exists
                authViewModel.checkUserExists(username)
                if (authViewModel.errorMessage != null) {
                    // If user already exists, do not proceed with registration
                }

                // Register user if all validations pass
                authViewModel.registerUser(username, password)
                onRegisterSuccess()
            } else {
                // Handle empty fields scenario
                authViewModel.errorMessage = "Please fill in all fields."
            }
        }) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToLogin) {
            Text("Go to Login")
        }
    }
}




