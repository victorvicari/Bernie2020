package com.appsontap.bernie2020

import android.app.Application
import android.util.Log
import com.appsontap.bernie2020.database.AppDatabase
import com.appsontap.bernie2020.util.TAG

/**
Feel the Bern
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        INSTANCE = this

        AppDatabase.getDatabase()
            .populateDatabase(this)
    }

    companion object {
        lateinit var INSTANCE: App

        fun get(): App {
            return INSTANCE
        }
    }
}