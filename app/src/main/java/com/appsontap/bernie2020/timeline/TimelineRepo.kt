package com.appsontap.bernie2020.timeline

import android.annotation.SuppressLint
import com.appsontap.bernie2020.AppDatabase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

/**
 * Feel the Bern
 */
class TimelineRepo{
    
     private val timeline = Timeline()
    
    @SuppressLint("CheckResult")
    fun buildTimeline(): Single<Timeline> {
        
        timeline.clear()
        
         return AppDatabase
            .getDatabase()
            .timelineDao()
            .getAll()
            .map { 
                it.sortedBy { it.year.toInt() }
                it
            }
            .flatMapObservable { it.toObservable() }
            .doOnNext {
                if (timeline.contains(it.year)) {
                    timeline.getSectionForYear(it.year)?.itemsForYear?.add(it)
                } else {
                    val section = Timeline().Section(it.year)
                    section.itemsForYear.add(it)
                    timeline.insert(section)
                }
            }.toList()
            .flatMap { 
                Observable.just(timeline).singleOrError()
            }
        
    }
}