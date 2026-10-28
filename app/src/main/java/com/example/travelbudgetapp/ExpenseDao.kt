package com.example.travelbudgetapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense_table ORDER BY id DESC")
    fun getAll(): Flow<List<Expense>>

    @Insert
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}