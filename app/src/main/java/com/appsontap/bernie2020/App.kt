package com.appsontap.bernie2020

import android.app.Application
import android.util.Log

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        INSTANCE = this

        AppDatabase.getDatabase(this)
            .populateDatabase(this)
    }

    companion object {
        lateinit var INSTANCE: App

        fun get(): App {
            return INSTANCE
        }
    }
}