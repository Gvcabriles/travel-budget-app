package com.example.travelbudgetapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_table")
data class Budget(
    @PrimaryKey val id: Int = 0,
    val amountAUD: Double
)
