package com.bblackbelt.data.di

import com.bblackbelt.data.JsonConverterWrapper
import com.bblackbelt.data.StocksRepository
import com.bblackbelt.data.StockDataService
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.Request

import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    fun provideRequestBuilder(): Request {
        return Request.Builder().url("ws://159.89.15.214:8080/").build()
    }

    @Provides
    fun provideGson(): Gson = Gson()
}

@Module
@InstallIn(ApplicationComponent::class)
interface DataModule {
    @Binds
    fun bindStockDataService(dataService: StockDataService.Impl): StockDataService

    @Binds
    fun bindJsonConverterWrapper(wrapper: JsonConverterWrapper.Impl): JsonConverterWrapper

    @Binds
    fun bindStockDataLoader(dataLoader: StocksRepository.Impl): StocksRepository
}

