package com.bblackbelt.domain

import com.bblackbelt.data.StockDataService
import com.bblackbelt.domain.mapper.map
import com.bblackbelt.domain.model.Stock
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

interface StockUseCase {

    fun loadStocks(isins: List<String>): Observable<Stock>

    class Impl @Inject constructor(private val service: StockDataService): StockUseCase {
        override fun loadStocks(isins: List<String>): Observable<Stock> {
            return service.loadStocks(isins).map { it.map() }
        }
    }
}
