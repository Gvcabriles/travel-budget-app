package com.example.travelbudgetapp

import android.util.Log
import com.example.travelbudgetapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CurrencyConverter {
    suspend fun convertToAUD(amount: Double, fromCurrency: String): Double {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getRates(
                    apiKey = BuildConfig.EXCHANGE_API_KEY,
                    base = fromCurrency
                )
                val audRate = response.conversion_rates["AUD"]
                if (audRate != null) {
                    val converted = amount * audRate
                    Log.d("CurrencyConverter", "Converted $amount $fromCurrency → $converted AUD")
                    converted
                } else {
                    Log.e("CurrencyConverter", "AUD rate missing in response")
                    0.0
                }
            } catch (e: Exception) {
                Log.e("CurrencyConverter", "Error: ${e.message}")
                0.0
            }
        }
    }
}
