package com.bblackbelt.exchanger.mapper

import com.bblackbelt.domain.model.Stock
import com.bblackbelt.exchanger.model.StockView

fun Stock.map(name: String): StockView {
    return StockView(name, isin, price)
}
