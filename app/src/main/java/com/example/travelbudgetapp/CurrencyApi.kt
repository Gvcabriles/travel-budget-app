package com.example.travelbudgetapp

import retrofit2.http.GET
import retrofit2.http.Path

data class ExchangeRateResponse(
    val result: String,
    val conversion_rates: Map<String, Double>
)

interface CurrencyApi {
    @GET("{apiKey}/latest/{base}")
    suspend fun getRates(
        @Path("apiKey") apiKey: String,
        @Path("base") base: String
    ): ExchangeRateResponse
}
