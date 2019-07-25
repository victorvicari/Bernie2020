package com.appsontap.bernie2020.timeline

import android.annotation.SuppressLint
import android.util.Log
import com.appsontap.bernie2020.AppDatabase
import com.appsontap.bernie2020.TAG
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class TimelineRepo{
    
     var timelineEmitter = BehaviorSubject.create<Timeline>()
     private val timeline = Timeline()
    
    @SuppressLint("CheckResult")
    private fun buildTimeline(){
        AppDatabase
            .getDatabase()
            .timelineDao()
            .getAll()
            .map { 
                it.sortedBy { it.year?.toInt() }
                it
            }.flatMapObservable { 
                it.toObservable()
            }
            .map { 
                if(timeline.contains(it.year)){
                    timeline.getSectionForYear(it.year)?.itemsForYear?.add(it)
                } else {
                    val section = Timeline().Section(it.year)
                    section.itemsForYear.add(it)
                    timeline.insert(section)
                }
            }.map { 
                timeline
            }.singleOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(onSuccess = {
                Log.d(TAG, "Built timeline $it")
                timelineEmitter.onNext(it)
            }, onError = {
                Log.e(TAG, "Couldn't build timeline ${it.message}", it)
            })
    }
}