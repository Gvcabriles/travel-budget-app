package com.example.travelbudgetapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import com.example.travelbudgetapp.ui.theme.TravelBudgetTheme

class MainActivity : ComponentActivity() {
    private val vm: BudgetViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelBudgetTheme {
                val budgetState = vm.budget.collectAsState()
                val expensesState = vm.expenses.collectAsState()

                val budget = budgetState.value?.amountAUD ?: 0.0
                val expenses = expensesState.value

                MainScreen(
                    budget = budget,
                    expenses = expenses,
                    onAddExpense = {title, description, amount, currency ->
                        vm.addExpense(title, description, amount, currency) },
                    onSetBudget = { vm.setBudget(it) },
                    onDeleteExpense = {vm.deleteExpense(it)}
                )
            }
        }
    }
}
