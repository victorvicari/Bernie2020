package com.appsontap.bernie2020.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpaper_table")
data class WallpaperItem(
    @PrimaryKey val id: String,
    val year: String,
    val wallpaper_resource: String?
    )