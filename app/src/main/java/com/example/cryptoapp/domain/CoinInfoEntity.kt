package com.example.cryptoapp.domain

data class CoinInfoEntity(
    val price: String?,
    val highDay: String?,
    val lowDay: String?,
    val lastUpdate: String,
    val fromSymbol: String,
    val toSymbol: String?,
    val imageUrl: String,
    val lastMarket: String?
)