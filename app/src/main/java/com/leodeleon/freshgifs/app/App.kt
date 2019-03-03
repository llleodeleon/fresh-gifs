package com.leodeleon.freshgifs.app

import android.app.Application
import com.leodeleon.freshgifs.di.appModule
import com.leodeleon.freshgifs.utils.logd
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.android.startKoin

class App: Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule))
        RxJavaPlugins.setErrorHandler {
            logd("ERROR", it.message?: "")
        }
    }
}