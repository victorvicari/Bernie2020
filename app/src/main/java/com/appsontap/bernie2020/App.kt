package com.appsontap.bernie2020

import android.app.Application
import android.util.Log
import com.appsontap.bernie2020.util.TAG
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.InputStreamReader

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

        parse()
    }

    companion object {
        lateinit var INSTANCE: App

        fun get(): App {
            return INSTANCE
        }
    }

    fun parse() {
        var json = JsonObject()
        var details = JsonArray()
        lateinit var currentLegislation : JsonObject

        val p = Pattern.compile("\"([^\"]*)\"")

        var reader = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.details)));
        var line = reader.readLine();
        while (line != null) {
//            println(line)
            if(line.trim().startsWith("legislation_id")){
                println("Starts with legislation_id")
                currentLegislation = JsonObject()
                val tokens = line.split(",")
                val key = tokens[0]
                val value = tokens[1]
                currentLegislation.addProperty(key, value)
                details.add(currentLegislation)

            }
            line = reader.readLine()
        }

        details.forEach{
            Log.d(TAG, it.toString())
        }
    }
}