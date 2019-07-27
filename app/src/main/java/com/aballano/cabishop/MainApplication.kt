package com.aballano.cabishop

import android.app.Application
import com.aballano.cabishop.checkout.sl.cartModule
import com.aballano.cabishop.common.sl.commonModule
import com.aballano.cabishop.productlist.sl.productModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(commonModule, productModule, cartModule))
        }
    }
}