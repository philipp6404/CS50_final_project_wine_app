package com.example.mywineappsimple

import android.app.Application
import com.example.mywineappsimple.data.AppContainer
import com.example.mywineappsimple.data.AppDataContainer

class WineApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}