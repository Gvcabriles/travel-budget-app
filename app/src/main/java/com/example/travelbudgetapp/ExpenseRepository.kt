package com.example.travelbudgetapp

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {
    fun getAllExpenses(): Flow<List<Expense>> = dao.getAll()
    suspend fun insert(expense: Expense) = dao.insert(expense)
}