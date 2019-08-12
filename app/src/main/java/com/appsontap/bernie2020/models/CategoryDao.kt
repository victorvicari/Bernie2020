package com.appsontap.bernie2020.models

import androidx.room.*
import io.reactivex.Single


@Dao
abstract class CategoryDao{
    @Query ("SELECT * FROM category_table")
     abstract fun getAll() : Single<List<Category>>
    
    @Query("SELECT * FROM category_table WHERE id = :categoryId")
    abstract fun getCategoryForId(categoryId: String) : Single<Category>
    
    @Query("SELECT * FROM category_table WHERE id IN (:ids)")
    abstract fun getCategoriesForids(ids: List<String>) : Single<List<Category>>
    
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(category: Category)
    
}