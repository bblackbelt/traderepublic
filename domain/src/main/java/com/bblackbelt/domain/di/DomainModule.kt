package com.bblackbelt.domain.di

import com.bblackbelt.domain.StockUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface DomainModule {

    @Binds
    fun bindStockUseCase(usecase: StockUseCase.Impl): StockUseCase
}
