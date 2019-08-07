package com.appsontap.bernie2020.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single


@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes_table")
    fun getAll(): Single<List<Quote>>

    @Query("SELECT * FROM quotes_table WHERE id = :quoteId")
    fun getQuote(quoteId: String): Single<Quote>
    
    @Query("SELECT * FROM quotes_table WHERE id IN (:ids)")
    fun getQuotesForids(ids: List<String>) : Single<List<Quote>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(quote: Quote)
}