package com.appsontap.bernie2020.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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
}