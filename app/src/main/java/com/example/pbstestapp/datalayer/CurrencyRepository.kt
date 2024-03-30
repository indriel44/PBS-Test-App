package com.example.pbstestapp.datalayer

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.logging.HttpLoggingInterceptor

interface CurrencyRepository {

    @GET("/daily_json.js")
    suspend fun getCurrencyList(): Valute

    companion object {
        val currencyRepository: CurrencyRepository = createCurrencyRepository()

        private fun createCurrencyRepository(): CurrencyRepository {


            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder().apply {
                addNetworkInterceptor(loggingInterceptor)
            }.build()


            val retrofit = Retrofit.Builder().apply {
                client(client)
                addConverterFactory(GsonConverterFactory.create())
                baseUrl("https://www.cbr-xml-daily.ru")
            }.build()


            return retrofit.create(CurrencyRepository::class.java)
        }
    }
}