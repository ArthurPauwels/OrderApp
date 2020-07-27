package com.example.orderapp

import android.app.Application
import timber.log.Timber

class OrderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}