package com.example.pbstestapp.domain

import com.example.pbstestapp.datalayer.Currency
import com.example.pbstestapp.datalayer.CurrencyRepository
import kotlin.math.round

class CurrencyProvider() {

    private val currencyRepository = CurrencyRepository.currencyRepository

    suspend fun getCurrencyList(): List<Currency> {
        val list: MutableList<Currency> = emptyList<Currency>().toMutableList()
        currencyRepository.getCurrencyList().list.forEach {
            val buffer = it.value
            buffer.value = round(buffer.value*100)/100
            list.add(buffer)
        }
        return list
    }
}
