package com.example.travelbudgetapp

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expense_table")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val title: String,
    val description: String,
    val amount: Double,
    val currencyCode: String,
    val convertedAmountInAud: Double,
    val dateTime: String
)
