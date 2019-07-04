package com.example.myapplication

import androidx.multidex.MultiDexApplication
import com.example.myapplication.di.KoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyAppApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(this@MyAppApplication)
            // use modules
            modules(KoinModules.appModule)
        }


    }
}