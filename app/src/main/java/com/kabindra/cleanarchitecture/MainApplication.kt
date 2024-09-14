package com.kabindra.cleanarchitecture

import android.app.Application
import com.kabindra.cleanarchitecture.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }
    }
}