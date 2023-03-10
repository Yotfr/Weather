package com.yotfr.weather

import android.app.Application
import android.content.Context
import com.yotfr.weather.di.AppComponent
import com.yotfr.weather.di.DaggerAppComponent

open class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDi()
    }

    private fun initDi() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .appContext(this)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }
