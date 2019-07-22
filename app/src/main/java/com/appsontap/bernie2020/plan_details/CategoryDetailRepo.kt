package com.appsontap.bernie2020.plan_details

import com.appsontap.bernie2020.AppDatabase
import com.appsontap.bernie2020.Constants
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class CategoryDetailRepo {

    //Build a list of all objects, with indexes to markate which ones are section headrs
    fun fetchDataForCategory(categoryId: String): Single<UiState.ListReady> {
        val result = mutableListOf<Any>()
        val titleIndexes = mutableSetOf<Int>()

        return AppDatabase
            .getDatabase()
            .categoryDao()
            .getCategoryForId(categoryId)
            .doOnSuccess {
                it?.name?.let { name ->
                    result.add(name)
                    titleIndexes.add(0)
                }
            }
            .flatMapObservable {
                AppDatabase.getDatabase().getPlansForCategory(it)?.toObservable()
            }
            .map {
                result.add("PLANS")
                titleIndexes.add(result.size - 1)
                it.forEach { plan ->
                    result.add(plan)
                }
            }.flatMap {
                AppDatabase.getDatabase().categoryDao().getCategoryForId(categoryId).toObservable()
            }.flatMap {
                AppDatabase.getDatabase().getLegislationForCategory(it).toObservable()
            }.map {
                result.add("Legislation")
                titleIndexes.add(result.size - 1)
                it.forEach { legislation ->
                    result.add(legislation)
                }
            }.flatMap {
                AppDatabase.getDatabase().categoryDao().getCategoryForId(categoryId).toObservable()
            }.flatMap {
                AppDatabase.getDatabase().getQuotesForCategory(it).toObservable()
            }.map {
                result.add("Quotes")
                titleIndexes.add(result.size - 1)
                it.forEach { quote ->
                    result.add(quote)
                }
            }.flatMap {
                Observable.just(UiState.ListReady(result, titleIndexes))
            }.singleOrError()
    }
}
