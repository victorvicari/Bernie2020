package com.appsontap.bernie2020.plans.model

import android.content.Context
import com.appsontap.bernie2020.database.AppDatabase
import io.reactivex.Single

/**
    Feel the Bern
 */
class PlansRepo(val context: Context) {
    var expandedItems: HashMap<Int, Boolean> = HashMap()
    private fun fetchCategoriesAndPlans(): Single<MutableList<Any>>? {
        return AppDatabase
            .getDatabase()
            .planDao()
            .getAll()
            .flattenAsObservable {
                it
            }
            .flatMap {
                AppDatabase.getDatabase().getCategoryWithPlans(it)
            }
            .toList()
    }

    fun onItemExpanded(categoryId: Int) {
        expandedItems[categoryId] = true
    }

    fun onItemCollapsed(categoryId: Int){
        expandedItems[categoryId] = false
    }

    fun fetchExpandedItems(): Single<List<Int>> {
        return Single.just(expandedItems.keys.filter { expandedItems[it] == true }.toList())
    }

}