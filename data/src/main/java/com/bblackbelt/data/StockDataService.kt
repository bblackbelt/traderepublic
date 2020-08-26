package com.bblackbelt.data

import com.bblackbelt.data.model.StockDto
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

interface StockDataService : StocksRepository {
    class Impl @Inject constructor(private val dataLoader: StocksRepository) : StockDataService {
        override fun loadStocks(isins: List<String>): Observable<StockDto> = dataLoader.loadStocks(isins)
    }
}
