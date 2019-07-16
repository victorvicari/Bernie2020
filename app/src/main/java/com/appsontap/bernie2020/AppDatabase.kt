package com.appsontap.bernie2020

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.models.CategoryDao
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.models.PlanDao
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
@Database (entities = arrayOf(Category::class, Plan::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearAllTables() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    abstract fun categoryDao() : CategoryDao
    
    abstract fun planDao() : PlanDao
    
    fun populateDatabase(context: Context){
        createCategories(context)
    }
    
    private fun createCategories(context: Context){
        val gson = GsonBuilder().create()
        var reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.categories)))
        val categoriesInput = reader.use { it.readText() }
        val categories = gson.fromJson(categoriesInput, Array<Category>::class.java)
        
        reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.proposals)))
        val plansInput = reader.use { it.readText() }
        val plans = gson.fromJson(plansInput, Array<Plan>::class.java)
        
        
        categories.toObservable()
            .doOnNext { category -> 
//                Log.d(TAG, category.toString())
                getDatabase(context).categoryDao().insert(category)
            }
            .flatMap { 
                plans.toObservable()
            }
            .doOnNext{
                plan -> 
//                Log.d(TAG, plan.toString())
                getDatabase(context).planDao().insert(plan)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy (
                onComplete = {
                    Log.d(TAG, "DB populated")
                },
                onError = {
                    Log.e(TAG, "Error writing to database ${it.message}", it)
                }
            )
    }
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, 
                        "app_database"
                    ).build()
                INSTANCE = instance
                return instance
            }
        }
   }

}