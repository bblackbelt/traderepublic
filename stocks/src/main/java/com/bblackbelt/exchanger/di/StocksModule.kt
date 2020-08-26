package com.bblackbelt.exchanger.di

import com.bblackbelt.exchanger.model.StocksIn
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object StocksModule {

    @Provides
    fun provideStocksIn(): StocksIn {
        val s = StocksIn()
        s.putAll(
            "US0231351067" to "Amazon",
            "US0378331005" to "Apple",
            "DE0007472060" to "Wirecard",
            "DE0005557508" to "Deutsche Telekom Aktie",
            "US4581401001" to "Intel corp.",
            "DE0007100000" to "Daimler",
            "US88160R1014" to "Tesla",
            "DE000BAY0017" to "Bayer",
            "DE0008232125" to "Lufthansa",
            "DE000CBK1001" to "CommkerzBank"
        )
        return s
    }
}
