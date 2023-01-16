package com.yotfr.weather.di

import dagger.Module

@Module(
    includes = [
        DataSourceModule::class,
        LocationModule::class,
        AppBindModule::class,
        UseCasesModule::class,
        AppBindModule::class
    ]
)
class AppModule
