package com.example.pbstestapp.datalayer

import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("Name")
    val name: String,

    @SerializedName("Value")
    var value: Float,
)