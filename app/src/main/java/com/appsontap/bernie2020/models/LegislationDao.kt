package com.appsontap.bernie2020.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

/**
 * Feel the Bern
 */
@Dao
interface LegislationDao {
    @Query("SELECT * FROM legislation_table")
    fun getAll() : Single<List<Legislation>>
    
    @Query("SELECT * FROM legislation_table WHERE id = :legislationId")
    fun getLegislation(legislationId: String) : Single<Legislation>
    
    @Query("SELECT * FROM legislation_table WHERE id IN (:ids)")
    fun getLegislationsForIds(ids: List<String>) : Single<List<Legislation>>
    
    @Insert (onConflict =  OnConflictStrategy.IGNORE)
    fun insert(legislation: Legislation)
}