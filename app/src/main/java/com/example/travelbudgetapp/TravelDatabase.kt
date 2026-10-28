package com.example.travelbudgetapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Budget::class, Expense::class], version = 1)
abstract class TravelDatabase : RoomDatabase(){
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao
}