package com.appsontap.bernie2020.wallpaper2

import android.annotation.SuppressLint
import com.appsontap.bernie2020.database.AppDatabase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

/**
 * Feel the Bern
 */
class Wallpaper2Repo{
    
     private val Wallpaper2b = Wallpaper2b()
    
    @SuppressLint("CheckResult")
    fun buildWallpaper2(): Single<Wallpaper2b> {
        
        Wallpaper2b.clear()
         return AppDatabase
            .getDatabase()
            .wallpaperDao()
            .getAll()
            .flatMapObservable { it.toObservable() }
            .doOnNext {
                Wallpaper2b.insert(it)
            }.toList()
            .flatMap { 
                Observable.just(Wallpaper2b).singleOrError()
            }
        
    }
}