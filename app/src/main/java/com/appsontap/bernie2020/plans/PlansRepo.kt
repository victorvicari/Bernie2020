package com.appsontap.bernie2020.plans

import android.content.Context
import android.util.Log
import com.appsontap.bernie2020.database.AppDatabase
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.util.TAG
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 
 */
class PlansRepo(val context: Context) {

    fun fetchData(): Single<MutableList<Any>>? {

        return AppDatabase
            .getDatabase()
            .planDao()
            .getAll()
           .flattenAsObservable {
                Log.d("in the flatten", it.toString())
                it }
            .filter {
                it.category_ids != null
            }
            .flatMap {
                AppDatabase.getDatabase().getCategoryWithPlans(it)
            }
            .toList()
    }

}