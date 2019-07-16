package com.appsontap.bernie2020.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans_table")
data class Plan(
    val category_ids: String?,
    val created_at: String?,
    val description: String?,
    @PrimaryKey val id: String,
    val legislation_ids: String?,
    val links: String?,
    val name: String?,
    val quote_ids: String?,
    val video_ids: String?
) {
    fun getCategoryIds(): List<String>? {
        category_ids?.let {
            return it.split(" ")
        }
        return null
    }
}