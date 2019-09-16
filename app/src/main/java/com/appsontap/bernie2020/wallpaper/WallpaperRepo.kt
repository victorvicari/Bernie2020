package com.appsontap.bernie2020.wallpaper

import android.annotation.SuppressLint
import com.appsontap.bernie2020.database.AppDatabase
import com.appsontap.bernie2020.models.WallpaperItem
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

/**
 * Feel the Bern
 */
class WallpaperRepo{

    private val Wallpapers = mutableListOf<WallpaperItem>()

    @SuppressLint("CheckResult")


    fun buildWallpaper(): Single<List<WallpaperItem>>  {

        Wallpapers.clear()
        return AppDatabase
            .getDatabase()
            .wallpaperDao()
            .getAll()
            .flatMapObservable { it.toObservable() }
            .doOnNext {
                Wallpapers.add(it)
            }.toList()
            .flatMap {
                Observable.just(Wallpapers).singleOrError()
            }
    }
}
