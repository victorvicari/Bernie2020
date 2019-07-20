package com.appsontap.bernie2020.models

import android.util.Log
import androidx.room.*
import com.appsontap.bernie2020.AppDatabase
import com.appsontap.bernie2020.TAG
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable


@Dao
abstract class CategoryDao{
    @Query ("SELECT * FROM category_table")
     abstract fun getAll() : Single<List<Category>>
    
    @Query("SELECT * FROM category_table WHERE id = :categoryId")
    abstract fun getCategoryForId(categoryId: String) : Single<Category>
    
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(category: Category)
    
}