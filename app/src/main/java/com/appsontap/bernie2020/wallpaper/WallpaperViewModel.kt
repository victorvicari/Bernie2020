package com.appsontap.bernie2020.wallpaper

import androidx.lifecycle.ViewModel
import com.appsontap.bernie2020.models.WallpaperItem
import io.reactivex.Single

/**
 * Feel the Bern
 */

class WallpaperViewModel : ViewModel(){

    private val repo = WallpaperRepo()

    fun WallpaperReady() : Single<List<WallpaperItem>> {
        return repo.buildWallpaper()
    }
}

