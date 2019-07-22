package com.appsontap.bernie2020.models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appsontap.bernie2020.AppDatabase
import com.appsontap.bernie2020.TAG
import io.reactivex.Observable

@Entity(tableName = "category_table")
data class Category(
    val description: String?,
    @PrimaryKey val id: String,
    val legislation_ids: String?,
    val links: String?,
    val main_quote_id: String?,
    val name: String?,
    val proposal_ids: String?,
    val quote_ids: String?,
    val related_category_ids: String?,
    val video_ids: String?
) {
    fun getLegislationIds(): List<String>? {
        legislation_ids?.let { 
            return it.split(" ")
        }
        return null
    }
    
    fun getPlanIds() : List<String>? {
        proposal_ids?.let { 
            return it.split(" ")
        }
        return null
    }

    fun getQuoteIds() : List<String>? {
        quote_ids?.let {
            return it.split(" ")
        }
        return null
    }
    
    
    fun getPlanWithCategory(category: Category): Observable<Any> {
        val getPlansObservable =
            Observable.fromArray(category.getPlanIds())
                .flatMapIterable {
                    it
                }.flatMap { id ->
                    Log.d(TAG, "plan id $id")
                    AppDatabase.getDatabase().planDao().getPlan(id).toObservable()
                }
        return Observable.concat(Observable.just(category), getPlansObservable)
}
}