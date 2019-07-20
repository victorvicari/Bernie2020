package com.appsontap.bernie2020.plans

import android.content.Context
import android.util.Log
import com.appsontap.bernie2020.AppDatabase
import com.appsontap.bernie2020.TAG
import com.appsontap.bernie2020.models.Category
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 
 */
class PlansRepo(val context: Context) {
    val dataEmitter = BehaviorSubject.create<MutableList<Any>>()
    
    fun fetchData(): Single<MutableList<Any>>? {
        return AppDatabase
            .getDatabase()
            .categoryDao()
            .getAll()
            .flattenAsObservable { it }
            .filter{
                it.proposal_ids != null
            }
            .flatMap {
                Log.d(TAG, "Category id ${it.id}")
                AppDatabase.getDatabase().getPlansWithCategory(it) 
            }
            .toList()
    }
}