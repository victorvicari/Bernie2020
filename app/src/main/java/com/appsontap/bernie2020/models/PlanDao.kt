package com.appsontap.bernie2020.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface PlanDao {
    @Query("SELECT * FROM plans_table")
    fun getAll(): Observable<List<Plan>>

    @Query("SELECT * FROM plans_table WHERE id = :planId")
    fun getPlan(planId: String) : Single<Plan>
    
    @Query("SELECT * FROM plans_table WHERE id IN (:ids)")
    fun getPlansForIds(ids: List<String>) : Single<List<Plan>>
    
    @Insert (onConflict =  OnConflictStrategy.IGNORE)
    fun insert(plan: Plan)
}