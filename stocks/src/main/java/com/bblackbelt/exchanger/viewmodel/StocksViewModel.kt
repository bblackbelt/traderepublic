package com.bblackbelt.exchanger.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bblackbelt.domain.StockUseCase
import com.bblackbelt.exchanger.mapper.map
import com.bblackbelt.exchanger.model.StockView
import com.bblackbelt.exchanger.model.StocksIn
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.Disposable

interface StocksViewModelInput {
    fun subscribeStockChanges()
    fun reload()
}
interface StocksViewModelOutput {
    fun onStockLoaded(): LiveData<List<StockView>>
    fun onError(): LiveData<String>
}

class StocksViewModel @ViewModelInject constructor(
    private val stocks: StocksIn,
    private val useCase: StockUseCase): ViewModel(), StocksViewModelInput, StocksViewModelOutput {

    val input: StocksViewModelInput
        get() = this

    val output: StocksViewModelOutput
        get() = this

    private var stockDisposable = Disposable.disposed()

    private val stockNotifier = MutableLiveData<List<StockView>>()
    private val error = MutableLiveData<String>()

    private val _dataSet = linkedMapOf<String, StockView>()

    override fun onStockLoaded(): LiveData<List<StockView>> = stockNotifier

    override fun subscribeStockChanges() {
        if (stockDisposable.isDisposed) {
            stockDisposable = useCase.loadStocks(stocks.getISINs())
                .map {
                    _dataSet[it.isin] = it.map(stocks.getName(it.isin))
                    _dataSet.values.toList()
                }
                .toFlowable(BackpressureStrategy.BUFFER)
                .subscribe({ stockNotifier.postValue(it) }, {
                    error.postValue(it.message ?: "doh")
                })
        }
    }

    override fun reload() {
        stockDisposable.dispose()
        subscribeStockChanges()
    }

    override fun onError(): LiveData<String> = error

    override fun onCleared() {
        super.onCleared()
        stockDisposable.dispose()
    }
}
