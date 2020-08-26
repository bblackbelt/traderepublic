package com.bblackbelt.data

import com.bblackbelt.data.model.StockDto
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Observable
import org.junit.Test


class StockDataServiceTest {

    @Test
    fun `test data loaded`() {
        val dataLoader = mock<StocksRepository>()
        whenever(dataLoader.loadStocks(listOf("abc"))).thenReturn(
            Observable.just(
                StockDto(
                    "abc",
                    12F
                )
            )
        )
        val dataService = StockDataService.Impl(dataLoader)
        dataService.loadStocks(listOf("abc")).test()
            .assertValue(StockDto("abc", 12F))
    }

    @Test
    fun `test data loaded failed`() {
        val dataLoader = mock<StocksRepository>()
        val error = Throwable()
        whenever(dataLoader.loadStocks(listOf("abc"))).thenReturn(Observable.error(error))
        val dataService = StockDataService.Impl(dataLoader)
        dataService.loadStocks(listOf("abc")).test()
            .assertError(error)
    }
}
