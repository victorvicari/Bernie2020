package com.appsontap.bernie2020.wallpaper2

import com.appsontap.bernie2020.models.WallpaperItem
import java.lang.RuntimeException

/**
 * Feel the Bern
 */
class Wallpaper2b {
    private var sections =mutableListOf<WallpaperItem>()

   fun insert(section: WallpaperItem) {
        sections.add(section)
    }

    fun totalItemCount() : Int {
        return sections.size
    }
    fun clear() {
        sections.clear()
    }
    //todo don't return Any return an enum or something
    fun getItemAtPosition(position: Int): Any {
        return sections[position]
    }

}
