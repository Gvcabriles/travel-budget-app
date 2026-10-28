package com.example.travelbudgetapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.room.Room
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        TravelDatabase::class.java,
        "travel_db"
    ).build()

    private val budgetDao = db.budgetDao()
    private val expenseDao = db.expenseDao()

    val budget = budgetDao.getBudget().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    val expenses = expenseDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setBudget(amount: Double){
        viewModelScope.launch {
            budgetDao.insert(Budget(id = 0, amountAUD = amount))
        }
    }

    fun addExpense(title: String, description: String, amount: Double, currencyCode: String) {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val convertedAmount = if (currencyCode.uppercase(Locale.ROOT) == "AUD") {
                amount
            } else {
                val result = CurrencyConverter.convertToAUD(amount, currencyCode.uppercase(Locale.ROOT))
                if (result == 0.0) {
                    Toast.makeText(context, "Failed to fetch exchange rate.", Toast.LENGTH_SHORT).show()
                }
                result
            }

            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            val expense = Expense(
                title = title,
                description = description,
                amount = amount,
                currencyCode = currencyCode,
                convertedAmountInAud = convertedAmount,
                dateTime = formattedDate
            )

            expenseDao.insert(expense)
            Log.d("BudgetViewModel", "Added $amount $currencyCode (${convertedAmount} AUD)")
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.delete(expense)
            Log.d("BudgetViewModel", "Deleted expense: ${expense.description}")
        }
    }
}
