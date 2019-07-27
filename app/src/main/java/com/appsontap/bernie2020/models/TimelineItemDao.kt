package com.appsontap.bernie2020.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
@Dao
interface TimelineItemDao{
    @Query("SELECT * FROM timeline_table")
    fun getAll() : Single<List<TimelineItem>>
    
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: TimelineItem)
}