package com.leodeleon.freshgifs.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.leodeleon.freshgifs.di.appModule
import org.koin.android.ext.android.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}