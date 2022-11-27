package com.example.cryptoapp.domain

class GetCoinInfoUseCase(private val repository: CoinRepository) {

    fun getCoinInfo(fromSymbol: String) = repository.getCoinInfo(fromSymbol)
}