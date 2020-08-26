package com.bblackbelt.exchanger.model

class StocksIn (private val data: LinkedHashMap<String, String> = LinkedHashMap()) {

    fun put(key: String, value: String)  {
        data[key] = value
    }

    fun putAll(vararg d: Pair<String, String>)  {
        d.forEach { put(it.first, it.second) }
    }

    fun getISINs() = data.keys.toList()

    fun getName(isin: String) = data[isin] ?: ""

}
