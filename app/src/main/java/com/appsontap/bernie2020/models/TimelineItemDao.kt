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
interface TimelineItemDao{
    @Query("SELECT * FROM timeline_table")
    fun getAll() : Single<List<TimelineItem>>
    
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: TimelineItem)
    
    @Query("SELECT * FROM timeline_table WHERE id IN (:ids)")
    fun getTimelineItemsForIds(ids: List<String>) : Single<List<TimelineItem>>
}