package com.example.financemanagementapp

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ExpenseRecordsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseRecordsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseRecordsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

