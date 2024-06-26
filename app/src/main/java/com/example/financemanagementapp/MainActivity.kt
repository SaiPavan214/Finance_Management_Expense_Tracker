package com.example.financemanagementapp
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financemanagementapp.ui.theme.FinanceManagementAppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 101
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 102
        private const val CHANNEL_ID = "finance_notifications"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannelBudget(this)
        createNotificationChannel(this)
        FirebaseApp.initializeApp(this)
        requestSmsPermissions()
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun proceedToApp() {
        handleIncomingIntent(intent)
        setContent {
            FinanceManagementAppTheme {
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                val firebaseUser: FirebaseUser? = auth.currentUser
                val currentUser = firebaseUser.toString()

                val viewModel: ExpenseRecordsViewModel by viewModels {
                    ExpenseRecordsViewModelFactory(
                        applicationContext
                    )
                }
                val incomeList = listOf(
                    Income(name = "Awards", iconResId = R.drawable.trophy, userId = currentUser),
                    Income(name = "Coupons", iconResId = R.drawable.coupons, userId = currentUser),
                    Income(name = "Grants", iconResId = R.drawable.grants, userId = currentUser),
                    Income(name = "Lottery", iconResId = R.drawable.lottery, userId = currentUser),
                    Income(name = "Refunds", iconResId = R.drawable.refund, userId = currentUser),
                    Income(name = "Rental", iconResId = R.drawable.rental, userId = currentUser),
                    Income(name = "Salary", iconResId = R.drawable.salary, userId = currentUser),
                    Income(name = "Sale", iconResId = R.drawable.sale, userId = currentUser)
                )

                val expenseList = listOf(
                    Expense(name = "Baby", iconResId = R.drawable.milk_bottle, userId = currentUser),
                    Expense(name = "Beauty", iconResId = R.drawable.beauty, userId = currentUser),
                    Expense(name = "Bills", iconResId = R.drawable.bill, userId = currentUser),
                    Expense(name = "Car", iconResId = R.drawable.car_wash, userId = currentUser),
                    Expense(name = "Clothing", iconResId = R.drawable.clothes_hanger, userId = currentUser),
                    Expense(name = "Education", iconResId = R.drawable.education, userId = currentUser),
                    Expense(name = "Electronics", iconResId = R.drawable.cpu, userId = currentUser),
                    Expense(name = "Entertainment", iconResId = R.drawable.confetti, userId = currentUser),
                    Expense(name = "Food", iconResId = R.drawable.diet, userId = currentUser),
                    Expense(name = "Health", iconResId = R.drawable.better_health, userId = currentUser),
                    Expense(name = "Home", iconResId = R.drawable.house, userId = currentUser),
                    Expense(name = "Insurance", iconResId = R.drawable.insurance, userId = currentUser),
                    Expense(name = "Shopping", iconResId = R.drawable.bag, userId = currentUser),
                    Expense(name = "Social", iconResId = R.drawable.social_media, userId = currentUser),
                    Expense(name = "Sport", iconResId = R.drawable.trophy, userId = currentUser),
                    Expense(name = "Transportation", iconResId = R.drawable.transportation, userId = currentUser)
                )

                LaunchedEffect(Unit) {
                    viewModel.insertInitialData(incomeList, expenseList)
                }
                val database = AppDatabase.getDatabase(applicationContext)
                val expenseDao = database.expenseDao()
                val navController = rememberNavController()
                val categoryToEdit = intent.getStringExtra("category_to_edit")
                val amount = intent.getDoubleExtra("amount", 0.0)
                val isIncome = intent.getBooleanExtra("isIncome", true)
                val authViewModel=AuthViewModel(applicationContext,auth,firestore)
                AuthenticationFlow(
                    navController,
                    authViewModel,
                    context = applicationContext,
                    startDestination = "splash",
                    categoryToEdit = categoryToEdit,
                    amount = amount,
                    isIncome = isIncome
                )
            }
        }
    }

    private fun requestSmsPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS
        )
        ActivityCompat.requestPermissions(this, permissions, SMS_PERMISSION_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_PERMISSION_REQUEST_CODE -> {
                val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
                if (allGranted) {
                    proceedToApp()
                } else {
                    Toast.makeText(this, "Permissions denied. Some features may not work.", Toast.LENGTH_SHORT).show()
                    proceedToApp()
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Finance Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for finance management"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        intent?.let {
            if (it.action == Intent.ACTION_VIEW) {
                val data = it.data
                // Handle the data if necessary
                Log.d("MainActivity", "Incoming data: $data")
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthenticationFlow(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    context: Context,
    startDestination: String,
    categoryToEdit: String?,
    amount: Double,
    isIncome: Boolean
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("splash") {
            SplashScreen(navController = navController, context = context)
        }
        composable("register") {
            RegistrationScreen(
                onRegisterSuccess = {
                    authViewModel.saveAuthState(true)
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { username, password ->
                    authViewModel.authenticate(
                        username = username as String,
                        password = password as String,
                        onSuccess = {
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onError = { error ->
                            // Handle error, for example, show error message
                        }
                    )
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                authViewModel = authViewModel
            )
        }
        composable("main") {
            FinanceManagementApp(
                context = context,
                categoryToEdit = categoryToEdit,
                amount = amount,
                isIncome = isIncome
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController, context: Context) {
    LaunchedEffect(Unit) {
        delay(3000) // Delay for 3 seconds
        // Check if it's the first launch and navigate accordingly
        val destination = if (isFirstLaunch(context)) "register" else "main"
        navController.navigate(destination) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your actual image resource
                contentDescription = "App Icon",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "FinanceManagementApp",
                fontSize = 24.sp,
                style = androidx.compose.material.MaterialTheme.typography.h4
            )
        }
    }
}

// Function to check if it's the first launch
fun isFirstLaunch(context: Context): Boolean {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    var isFirstLaunch = prefs.getBoolean("isFirstLaunch", true)

    if (!isFirstLaunch) {
        // Reset isFirstLaunch if the user has already launched the app
        val authStatePrefs = context.getSharedPreferences("auth_state", Context.MODE_PRIVATE)
        val isAuthenticated = authStatePrefs.getBoolean("is_authenticated", false)

        if (isAuthenticated) {
            isFirstLaunch = true
            prefs.edit().putBoolean("isFirstLaunch", isFirstLaunch).apply()
        }
    }

    return isFirstLaunch
}


@Composable
fun RequestPermissions(
    context: Context,
    onPermissionsResult: (Boolean) -> Unit
) {
    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        onPermissionsResult(allGranted)
    }

    LaunchedEffect(Unit) {
        val permissionsNeeded = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(Manifest.permission.READ_SMS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissionsNeeded.isNotEmpty()) {
            permissionsLauncher.launch(permissionsNeeded.toTypedArray())
        } else {
            onPermissionsResult(true)
        }
    }
}

