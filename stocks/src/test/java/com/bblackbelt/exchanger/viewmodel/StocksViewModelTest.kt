package com.bblackbelt.exchanger.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bblackbelt.domain.StockUseCase
import com.bblackbelt.domain.model.Stock
import com.bblackbelt.exchanger.model.StockView
import com.bblackbelt.exchanger.model.StocksIn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Observable
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class StocksViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val stocks = StocksIn().apply {
        put("abcd", "name")
    }

    private val stockUseCase = mock<StockUseCase>()

    val viewModel = StocksViewModel(stocks, stockUseCase)

    @Test
    fun `test data loaded`() {
        whenever(stockUseCase.loadStocks(listOf("abcd")))
            .thenReturn(Observable.just(Stock("abcd", 1.2f)))

        viewModel.input.subscribeStockChanges()

        viewModel.output.onStockLoaded().observeForever {  }

        assert(viewModel.output.onStockLoaded().value == listOf(StockView("name", "abcd", 1.2f)) )
    }

    @Test
    fun `test on error`() {
        val error = Exception("theError")
        whenever(stockUseCase.loadStocks(listOf("abcd")))
            .thenReturn(Observable.error(error))

        viewModel.input.subscribeStockChanges()

        viewModel.output.onError().observeForever {  }

        assert(viewModel.output.onError().value == "theError")
    }

    @Test
    fun `test on error default`() {
        val error = Exception()
        whenever(stockUseCase.loadStocks(listOf("abcd")))
            .thenReturn(Observable.error(error))

        viewModel.input.subscribeStockChanges()

        viewModel.output.onError().observeForever {  }

        assert(viewModel.output.onError().value == "doh")
    }

    @Test
    fun `test reload`() {
        val error = Exception("theError")
        whenever(stockUseCase.loadStocks(listOf("abcd")))
            .thenReturn(Observable.error(error), Observable.just(Stock("abcd", 1.2f)))

        viewModel.input.subscribeStockChanges()

        viewModel.output.onError().observeForever {  }
        viewModel.output.onStockLoaded().observeForever {  }

        assert(viewModel.output.onError().value == "theError")

        viewModel.input.reload()

        assert(viewModel.output.onStockLoaded().value == listOf(StockView("name", "abcd", 1.2f)) )
    }
}
