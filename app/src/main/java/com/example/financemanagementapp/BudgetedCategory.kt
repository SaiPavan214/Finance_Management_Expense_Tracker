package com.example.financemanagementapp


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.YearMonth

@Entity(tableName = "budgeted_categories")
data class BudgetedCategory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val limit: Double,
    val spent: Double,
    val remaining: Double,
    val monthYear: YearMonth,
    val dateTime: LocalDateTime,
    val userId: String // Include userId as a field in BudgetedCategory
)
