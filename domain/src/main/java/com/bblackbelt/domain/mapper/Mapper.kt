package com.bblackbelt.domain.mapper

import com.bblackbelt.data.model.StockDto
import com.bblackbelt.domain.model.Stock

fun StockDto.map(): Stock = Stock(isin, price)
