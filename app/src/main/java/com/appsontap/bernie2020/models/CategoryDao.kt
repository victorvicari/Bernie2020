package com.appsontap.bernie2020.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface CategoryDao{
    @Query ("SELECT * FROM category_table")
    fun getAll() : Single<List<Category>>
    
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Category)
}