package com.kabindra.cleanarchitecture.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            provideAppModule,
            provideHttpClientModule,
            provideApiServiceModule,
            provideDataSourceModule,
            provideRepositoryModule,
            provideUseCaseModule,
            provideViewModelModule
        )
    }
}