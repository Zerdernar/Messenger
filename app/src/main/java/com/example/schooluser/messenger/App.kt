package com.example.schooluser.messenger

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.soloader.SoLoader
import org.koin.android.ext.android.startKoin
import timber.log.Timber

//инициализатор
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)
        startKoin(
            this, listOf(
                AppModule
            )
        )
    }
}