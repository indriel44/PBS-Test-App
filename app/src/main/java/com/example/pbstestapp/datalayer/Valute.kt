package com.example.pbstestapp.datalayer

import com.google.gson.annotations.SerializedName

data class Valute(

    @SerializedName("Valute")
    val list: Map<String, Currency>,

    )