package com.appsontap.bernie2020.timeline

import androidx.lifecycle.ViewModel
import io.reactivex.Single

/**
 * Feel the Bern
 */
class TimelineViewModel : ViewModel(){

    private val repo = TimelineRepo()

    fun timelineReady() : Single<Timeline> {
        return repo.buildTimeline()
    }
}