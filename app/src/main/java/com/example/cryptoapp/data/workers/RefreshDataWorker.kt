package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.*
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.network.ApiService
import kotlinx.coroutines.delay

class RefreshDataWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val coinInfoDao: CoinInfoDao,
    private val apiService: ApiService,
    private val mapper: CoinMapper
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        while (true) {
            kotlin.runCatching {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fromSymbol = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = fromSymbol)
                val infoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                val dbModelList = infoDtoList.map { mapper.mapDtoToDbModel(it) }
                coinInfoDao.insertPriceList(dbModelList)
            }.getOrNull()
            delay(10000)
        }
    }

    companion object {
        const val NAME = "RefreshDataWorker"
        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<RefreshDataWorker>().build()
        }
    }
}