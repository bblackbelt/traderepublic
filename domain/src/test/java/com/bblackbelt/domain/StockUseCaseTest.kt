package com.bblackbelt.domain

import com.bblackbelt.data.StockDataService
import com.bblackbelt.data.model.StockDto
import com.bblackbelt.domain.model.Stock
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

class StockUseCaseTest {

    @Test
    fun `test data loaded`() {
        val service = mock<StockDataService>()
        whenever(service.loadStocks(listOf("abc"))).thenReturn(
            Observable.just(
                StockDto(
                    "abc",
                    12F
                )
            )
        )
        val useCase = StockUseCase.Impl(service)
        useCase.loadStocks(listOf("abc")).test()
            .assertValue(Stock("abc", 12F))
    }

    @Test
    fun `test data loaded failed`() {
        val service = mock<StockDataService>()
        val error = Throwable()
        whenever(service.loadStocks(listOf("abc"))).thenReturn(Observable.error(error))
        val useCase = StockUseCase.Impl(service)
        useCase.loadStocks(listOf("abc")).test()
            .assertError(error)
    }
}
