package com.appsontap.bernie2020.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

/**
 * Feel the Bern
 */
@Dao
interface WallPaperItemDao{
    @Query("SELECT * FROM wallpaper_table")
    fun getAll() : Single<List<WallpaperItem>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: WallpaperItem)
    
    @Query("SELECT * FROM wallpaper_table WHERE id IN (:ids)")
    fun getWallpaper2ItemsForIds(ids: List<String>) : Single<List<WallpaperItem>>
}