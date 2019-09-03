package com.appsontap.bernie2020.wallpaper2

import androidx.lifecycle.ViewModel
import io.reactivex.Single

/**
 * Feel the Bern
 */
class Wallpaper2ViewModel : ViewModel(){

    private val repo = Wallpaper2Repo()

    fun Wallpaper2Ready() : Single<Wallpaper2b> {
        return repo.buildWallpaper2()
    }
}