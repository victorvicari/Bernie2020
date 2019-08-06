package com.appsontap.bernie2020

import android.app.Application
import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

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