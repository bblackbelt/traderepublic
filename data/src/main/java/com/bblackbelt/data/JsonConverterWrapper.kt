package com.bblackbelt.data

import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject

interface JsonConverterWrapper {

    fun toJson(obj: Any): String
    fun <T> fromJson(jsonString: String, classOfT: Class<T>): T
    fun <T> fromJson(jsonString: String, type: Type): T

    class Impl @Inject constructor(private val gson: Gson) : JsonConverterWrapper {
        override fun toJson(obj: Any): String = gson.toJson(obj)

        override fun <T> fromJson(jsonString: String, classOfT: Class<T>): T =
            gson.fromJson(jsonString, classOfT)

        override fun <T> fromJson(jsonString: String, type: Type): T =
            gson.fromJson(jsonString, type)
    }
}
