package com.yotfr.weather

import android.app.Application
import com.yotfr.weather.di.AppComponent
import com.yotfr.weather.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}
