package com.appsontap.bernie2020.plans

import android.content.Context
import android.util.Log
import com.appsontap.bernie2020.database.AppDatabase
import com.appsontap.bernie2020.util.TAG
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 
 */
class PlansRepo(val context: Context) {

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