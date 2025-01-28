package com.example.mytasks

import android.app.Application
import com.example.mytasks.data.di.androidModule
import com.example.mytasks.data.di.appModule
import com.example.mytasks.data.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyTasksApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyTasksApplication)
            modules(
                appModule,
                storageModule,
                androidModule
            )
        }
    }
}