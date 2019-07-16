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
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class PlansRepo(val context: Context) {
    val dataEmitter = BehaviorSubject.create<MutableList<Any>>()
    
    fun fetchData(): Single<MutableList<Any>>? {
        return AppDatabase
            .getDatabase(context)
            .categoryDao()
            .getAll()
            .flattenAsObservable { it }
            .filter{
                it.proposal_ids != null
            }
            .flatMap {
                Log.d(TAG, "Category id ${it.id}")
                getPlanWithCategory(it) 
            }
            .toList()
    }


    fun getPlanWithCategory(category: Category): Observable<Any> {
        val getPlansObservable =
            Observable.fromArray(category.getPlanIds())
                .flatMapIterable {
                    it
                }.flatMap { id ->
                    Log.d(TAG, "plan id $id")
                    AppDatabase.getDatabase(context).planDao().getPlan(id).toObservable()
                }
        return Observable.concat(Observable.just(category), getPlansObservable)
}

//    fun fetchCategoriesAndPlans() {
//        val items = mutableListOf<Any>()
//        AppDatabase
//            .getDatabase(context)
//            .categoryDao()
//            .getAll()
//            .concatMap { listOfCategories ->
//                listOfCategories.toObservable()
//            }
//            .doOnNext { category ->
//                items.add(category)
//            }
//            .concatMap { category ->
//                category.getPlanIds()!!.toObservable()
//            }
//            .flatMap { planId ->
//                AppDatabase.getDatabase(context).planDao().getPlan(planId)
//            }.collectInto(items, BiConsumer{ list, i ->
//                Log.d(TAG, "Collect into")
//                list.add(i)
//            })
//            .subscribeBy(
//                onSuccess = {
//                    Log.d(TAG, "Got the list")
//                },
//                onError = {
//                    Log.e(TAG, "Couldn't build list ${it.message}", it)
//                })
//    }
}