package com.appsontap.bernie2020

import android.app.Application
import android.util.Log

/**
 
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        INSTANCE = this

        AppDatabase.getDatabase()
            .populateDatabase(this)
    }

//    override fun getSystemService(name: String): Any? {
//        if(name == Constants.FRAGMENT_MANAGER){
//            return 
//        }
//        return super.getSystemService(name)
//    }

    companion object {
        lateinit var INSTANCE: App

        fun get(): App {
            return INSTANCE
        }
    }
}