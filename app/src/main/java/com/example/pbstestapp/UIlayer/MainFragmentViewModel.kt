package com.example.pbstestapp.UIlayer

import androidx.lifecycle.ViewModel
import com.example.pbstestapp.datalayer.Currency
import com.example.pbstestapp.domain.CurrencyProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

class MainFragmentViewModel : ViewModel() {

    private val currencyProvider = CurrencyProvider()

    val currencyList: Flow<List<Currency>> = channelFlow {
        withContext(Dispatchers.IO) {
            send(currencyProvider.getCurrencyList())
        }
    }


}