package com.appsontap.bernie2020.plan_details

import com.appsontap.bernie2020.App
import com.appsontap.bernie2020.AppDatabase
import com.appsontap.bernie2020.Constants
import com.appsontap.bernie2020.R
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

/**
 * Feel the Bern
 */
class CategoryDetailRepo {

    fun fetchDataForPlan(planId: String): Single<UiState.ListReady> {
        val result = mutableListOf<Any>()
        val titleIndexes = mutableSetOf<Int>()

        return AppDatabase
            .getDatabase()
            .planDao()
            .getPlan(planId)
            .doOnSuccess {
                it.name?.let { name ->
                    result.add(name)
                    titleIndexes.add(0)
                }

            }.map {
                it.getCategoryIds()?.first()
            }.flatMap {
                AppDatabase.getDatabase().categoryDao().getCategoryForId(it)
            }.flatMap {
                AppDatabase.getDatabase().getLegislationForCategory(it)
            }.map {
                result.add(App.get().resources.getString(R.string.detailed_plans_and_bills))
                titleIndexes.add(result.size - 1)
                //add all the legislation for the first category
                it.forEach { legislation ->
                    result.add(legislation)
                }
            }.flatMap {
                AppDatabase.getDatabase().planDao().getPlan(planId)
            }.flatMap {
                AppDatabase.getDatabase().getCategoriesForPlan(it)
            }.map {
                result.add(App.get().resources.getString(R.string.more_plans))
                titleIndexes.add(result.size - 1)
                it.forEach { category ->
                    result.add(category)
                }
            }.flatMapObservable {
                Observable.just(UiState.ListReady(result, titleIndexes))
            }.singleOrError()
    }

    //Build a list of all objects, with indexes to markate which ones are section headers
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
                result.add(App.get().resources.getString(R.string.plans_and_proposals))
                titleIndexes.add(result.size - 1)
                it.forEach { plan ->
                    result.add(plan)
                }
            }.flatMap {
                AppDatabase.getDatabase().categoryDao().getCategoryForId(categoryId).toObservable()
            }.flatMap {
                AppDatabase.getDatabase().getLegislationForCategory(it).toObservable()
            }.map {
                result.add(App.get().resources.getString(R.string.detailed_plans_and_bills))
                titleIndexes.add(result.size - 1)
                it.forEach { legislation ->
                    result.add(legislation)
                }
            }.flatMap {
                AppDatabase.getDatabase().categoryDao().getCategoryForId(categoryId).toObservable()
            }.flatMap {
                AppDatabase.getDatabase().getQuotesForCategory(it).toObservable()
            }.map {
                result.add(App.get().resources.getString(R.string.statements))
                titleIndexes.add(result.size - 1)
                it.forEach { quote ->
                    result.add(quote)
                }
            }.flatMap {
                Observable.just(UiState.ListReady(result, titleIndexes))
            }.singleOrError()
    }

}
