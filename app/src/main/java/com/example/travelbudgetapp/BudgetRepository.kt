package com.example.travelbudgetapp

import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val dao: BudgetDao) {
    fun getBudget(): Flow<Budget?> = dao.getBudget()
    suspend fun setBudget(amount: Double) = dao.insert(Budget(0, amount))
    suspend fun deduct(amount: Double) {
        val current = dao.getBudget()
    }
}