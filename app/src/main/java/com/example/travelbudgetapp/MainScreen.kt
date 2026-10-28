package com.example.travelbudgetapp

import android.icu.util.Calendar
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    budget: Double,
    expenses: List<Expense>,
    onAddExpense: (String, String, Double, String) -> Unit,
    onSetBudget: (Double) -> Unit,
    onDeleteExpense: (Expense) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var showBudgetInput by remember { mutableStateOf(false) }

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 0..11 -> "Good Morning!"
            in 12..17 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        },
        topBar = {
            TopAppBar(title = { Text(greeting) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (budget == 0.0) {
                Button(onClick = { showBudgetInput = true }) {
                    Text("Set Budget (AUD)")
                }
            } else {
                val totalSpent = expenses.sumOf { it.convertedAmountInAud }
                val remaining = budget - totalSpent
                val progress = (totalSpent / budget).coerceIn(0.0, 1.0)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Budget: $${String.format("%,.2f", budget)} AUD")
                        Text("Remaining: $${String.format("%,.2f", remaining)} AUD")
                    }

                    // ✏️ Edit Budget Icon
                    IconButton(
                        onClick = { showBudgetInput = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Budget",
                            tint = Color(0xFF1976D2)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Budget Usage", style = MaterialTheme.typography.labelLarge)

                val animatedProgress by animateFloatAsState(
                    targetValue = progress.toFloat(),
                    animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
                )

                BudgetProgressBar(
                    progress = animatedProgress.toDouble(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Text(
                    text = "${(progress * 100).toInt()}% of budget spent",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(expenses) { expense ->
                    ExpenseCard(
                        expense = expense,
                        onDelete = {onDeleteExpense(expense) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            onDismiss = { showDialog = false },
            onSave = { title, desc, amt, curr ->
                onAddExpense(title, desc, amt, curr)
                showDialog = false
            }
        )
    }

    if (showBudgetInput) {
        BudgetInputDialog(
            onDismiss = { showBudgetInput = false },
            onSave = { amount ->
                onSetBudget(amount)
                showBudgetInput = false
            }

        )
    }
}

@Composable
fun ExpenseCard(expense: Expense, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.padding(12.dp)) {
                Text(expense.title, style = MaterialTheme.typography.titleMedium)
                Text(expense.description, style = MaterialTheme.typography.titleMedium)
                Text("${expense.amount} ${expense.currencyCode}")
                if (expense.currencyCode != "AUD") {
                    Text("(≈ ${"%.2f".format(expense.convertedAmountInAud)} AUD)")
                }
                Text(expense.dateTime, style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Expense",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun BudgetInputDialog(onDismiss: () -> Unit, onSave: (Double) -> Unit) {
    var amount by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (amount.isNotBlank()) {
                    onSave(amount.toDouble())
                    onDismiss()
                }
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Set Budget") },
        text = {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Budget Amount (AUD)") }
            )
        }
    )
}

@Composable
fun BudgetProgressBar(
    progress: Double,
    modifier: Modifier = Modifier
) {
    val color = when {
        progress < 0.5 -> Color(0xFF4CAF50) // Green
        progress < 0.8 -> Color(0xFFFFC107) // Amber
        else -> Color(0xFFF44336)           // Red
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp)) // Rounded ends
            .background(Color(0xFFE0E0E0))   // Track color
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = progress.toFloat())
                .clip(RoundedCornerShape(25.dp))
                .background(color)
        )
    }
}
