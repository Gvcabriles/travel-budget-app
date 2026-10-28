package com.example.travelbudgetapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget_table WHERE id = 0")
    fun getBudget(): Flow<Budget?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)
}