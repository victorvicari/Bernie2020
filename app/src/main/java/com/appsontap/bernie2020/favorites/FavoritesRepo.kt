package com.appsontap.bernie2020.favorites

import android.content.Context
import com.appsontap.bernie2020.database.AppDatabase
import com.appsontap.bernie2020.util.loadFavoritesFromSharedPrefs
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class FavoritesRepo(val context: Context) {
    val dataEmitter = BehaviorSubject.create<MutableList<Any>>()

    fun fetchData() : Single<List<Any>> {
        val favoriteIds = context.loadFavoritesFromSharedPrefs().toSortedSet()
        val planIds = mutableListOf<String>()
        val legislationIds = mutableListOf<String>()
        for (id in favoriteIds) {
            if (id.substring(0, 1) == "p") {
                planIds.add(id)
            }
            if (id.substring(0, 1) == "l") {
                legislationIds.add(id)
            }
        }
        return AppDatabase.getDatabase().getFavorites(planIds,legislationIds)
    }

    fun fetchPlanData(): Single<List<Any>>? {
        // gather favorite plans, favorites, TODO quotes, vids, etc
        val favoriteIds = context.loadFavoritesFromSharedPrefs().toSortedSet()
        val planIds = mutableListOf<String>()
        val legislationIds = mutableListOf<String>()
        for (id in favoriteIds) {
            if (id.substring(0, 1) == "p") {
                planIds.add(id)
            }
            if (id.substring(0, 1) == "l") {
                legislationIds.add(id)
            }
        }

        return AppDatabase.getDatabase()
            .planDao()
            .getPlansForIds(planIds)
            .map { it }
         //the following leads to java.lang.IllegalArgumentException: Sequence contains more than one element!
    }

    fun fetchLegislationData(): Single<List<Any>>? {
        // gather favorite plans, favorites, TODO quotes, vids, etc
        val favoriteIds = context.loadFavoritesFromSharedPrefs().toSortedSet()
        val planIds = mutableListOf<String>()
        val legislationIds = mutableListOf<String>()
        for (id in favoriteIds) {
            if (id.substring(0, 1) == "p") {
                planIds.add(id)
            }
            if (id.substring(0, 1) == "l") {
                legislationIds.add(id)
            }
        }

        return AppDatabase.getDatabase()
            .legislationDao()
            .getLegislationsForIds(legislationIds)
            .map { it }
    }
}


