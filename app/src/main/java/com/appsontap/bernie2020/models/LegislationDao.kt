package com.appsontap.bernie2020.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
@Dao
interface LegislationDao {
    @Query("SELECT * FROM legislation_table")
    fun getAll() : Single<List<Legislation>>
    
    @Query("SELECT * FROM legislation_table WHERE id = :legislationId")
    fun getLegislation(legislationId: String) : Single<Legislation>
    
    @Insert (onConflict =  OnConflictStrategy.ABORT)
    fun insert(legislation: Legislation)
}