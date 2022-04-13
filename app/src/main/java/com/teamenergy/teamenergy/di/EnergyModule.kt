package com.teamenergy.teamenergy.di

import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.teamenergy.net.EnergyDataSource
import com.teamenergy.teamenergy.net.createOkHttpClient
import com.teamenergy.teamenergy.net.createWebService
import com.teamenergy.teamenergy.repository.BaseEnergyRepository
import com.teamenergy.teamenergy.repository.BaseEnergyRepositoryImpl
import org.koin.dsl.module

fun appModule() = module {
    single { createWebService<EnergyDataSource>(createOkHttpClient()) }
    single<BaseEnergyRepository> { BaseEnergyRepositoryImpl(get()) }
    single { BaseEnergyViewModel(get()) }
}