package com.appsontap.bernie2020.timeline

import androidx.lifecycle.ViewModel
import io.reactivex.Single

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class TimelineViewModel : ViewModel(){

    val repo = TimelineRepo()

    fun timelineReady() : Single<Timeline> {
        return repo.buildTimeline()
    }
    override fun onCleared() {
        super.onCleared()
    }
}