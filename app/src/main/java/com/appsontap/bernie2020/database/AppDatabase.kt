package com.appsontap.bernie2020.database

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.appsontap.bernie2020.App
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.models.*
import com.appsontap.bernie2020.util.Converters
import com.appsontap.bernie2020.util.TAG
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

/**

 */
@Database(entities = [Category::class, Plan::class, Quote::class, Legislation::class, TimelineItem::class,WallpaperItem::class], version = 1)
@TypeConverters(Converters::class)
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
    
    

    abstract fun categoryDao(): CategoryDao

    abstract fun planDao(): PlanDao

    abstract fun quoteDao(): QuoteDao
    
    abstract fun legislationDao(): LegislationDao
    
    abstract fun timelineDao(): TimelineItemDao

    abstract fun wallpaperDao(): WallPaperItemDao

    @SuppressLint("CheckResult")
    fun populateDatabase(context: Context) {
        val gson = GsonBuilder().create()
        var reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.categories)))
        val categoriesInput = reader.use { it.readText() }
        val categories = gson.fromJson(categoriesInput, Array<Category>::class.java)

        reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.proposals)))
        val plansInput = reader.use { it.readText() }
        val plans = gson.fromJson(plansInput, Array<Plan>::class.java)

        reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.quotes)))
        val quotesInput = reader.use { it.readText() }
        val quotes = gson.fromJson(quotesInput, Array<Quote>::class.java)
        
        reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.legislation)))
        val legislationInput = reader.use { it.readText() }
        val legislation = gson.fromJson(legislationInput, Array<Legislation>::class.java)
        
        reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.timeline)))
        val timelineInput = reader.use { it.readText() }
        val timeline = gson.fromJson(timelineInput, Array<TimelineItem>::class.java)

        reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.wallpaper))) //EdChange
        val wallpaperInput = reader.use { it.readText() }
        val wallpaper = gson.fromJson(wallpaperInput, Array<WallpaperItem>::class.java)

        categories.toObservable()
            .doOnNext { category ->
                getDatabase().categoryDao().insert(category)
            }
            .flatMap {
                plans.toObservable()
            }
            .doOnNext { plan ->
                getDatabase().planDao().insert(plan)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    Log.d(TAG, "Built categories and plans")
                },
                onError = {
                    Log.e(TAG, "Error writing to database ${it.message}", it)
                }
            )
        
        legislation
            .toObservable()
            .doOnNext{
                getDatabase().legislationDao().insert(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    Log.d(TAG, "Built legislation")
                },
                onError = {
                    Log.w(TAG, "Did not build legislations \n ${it.message}", it)
                }
            )
        
        
        
        quotes.toObservable()
            .doOnNext { quote ->
                getDatabase().quoteDao().insert(quote)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    Log.d(TAG, "Built quotes")
                },
                onError = {
                    Log.e(TAG, "Couldn't build quotes ${it.message}", it)
                }
            )
        
        timeline.toObservable()
            .doOnNext{
                getDatabase().timelineDao().insert(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    Log.d(TAG, "Built timeline")
                },
                onError = {
                    Log.e(TAG, "Couldn't build timeline ${it.message}", it)
                }
            )

        wallpaper.toObservable()
            .doOnNext{
                getDatabase().wallpaperDao().insert(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {
                    Log.d(TAG, "Built Wallpaper")
                },
                onError = {
                    Log.e(TAG, "Couldn't build wallpaper ${it.message}", it)
                }
            )
    }
    
    //can't @Transaction these since they return Rx async types, this is a Room limitation
    open fun getCategoriesForPlan(plan: Plan): Single<MutableList<Category>>? {
        return Observable
            .just(plan)
            .flatMap { 
                Observable.fromArray(it.getCategoryIds())
            }
            .flatMapIterable { it }
            .flatMap { 
                getDatabase().categoryDao().getCategoryForId(it).toObservable()
            }.toList()
    }
    
    open fun getPlansForCategory(category: Category): Single<MutableList<Plan>>? {
        return Observable
            .just(category)
            .flatMap {
                Observable.fromArray(it.getPlanIds())
            }
            .flatMapIterable { it }
            .flatMap {
                getDatabase().planDao().getPlan(it).toObservable()
            }.toList()
    }
    
    fun getLegislationForCategory(category: Category) : Single<List<Legislation>>{
        return Observable
            .just(category)
            .filter{
                it.getLegislationIds() != null
            }
            .flatMap { 
                Observable.fromArray(it.getLegislationIds())
            }
            .flatMapIterable { it }
            .flatMap { 
                Log.d(TAG, "Fetching legislation: $it for category: ${category.id}")
                getDatabase().legislationDao().getLegislation(it).toObservable()
            }.toList()
    }

    fun getQuotesForCategory(category: Category): Single<List<Quote>> {
        return Observable
            .just(category)
            .filter{
                it.getQuoteIds() != null
            }
            .flatMap {
                Observable.fromArray(it.getQuoteIds())
            }.flatMapIterable {
                it
            }
            .flatMap {
                Log.d(TAG, "Fetching quote: $it for category: ${category.id}")
                getDatabase().quoteDao().getQuote(it).toObservable()
            }.toList()
    }

    open fun getPlansWithCategory(category: Category): Observable<Any> {
        val getPlansObservable =
            Observable.fromArray(category.getPlanIds())
                .flatMapIterable {
                    it
                }.flatMap { id ->
                    Log.d(TAG, "plan id $id")
                    getDatabase().planDao().getPlan(id).toObservable()
                }
        return Observable.concat(Observable.just(category), getPlansObservable)
    }
    
    open fun getFavorites(planIds: List<String>, legIds: List<String>): Single<List<Any>> {
        val getPlansObservable = 
            Observable.fromArray(planIds)
                .flatMapIterable { 
                    it
                }
                .flatMap { id ->
                     Log.d(TAG, "Fetching favorited plan id $id")
                    getDatabase().planDao().getPlan(id).toObservable()
                }
        
        val getLegislationsObservable = 
            Observable.fromArray(legIds)
                .flatMapIterable { it }
                .flatMap { id -> 
                    Log.d(TAG, "Fetching favorited legislation id $id")
                    getDatabase().legislationDao().getLegislation(id).toObservable()
                }
        
        return Observable.concat(getPlansObservable, getLegislationsObservable).toList() as Single<List<Any>>
    }


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    App.get(),
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}