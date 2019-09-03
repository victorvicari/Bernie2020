package com.appsontap.bernie2020.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes_table")
data class Quote(
    val category_ids: String?,
    val created_at: String?,
    @PrimaryKey val id: String,
    val legislation_ids: String?,
    val other_links: String?,
    val proposal_ids: String?,
    val quote: String,
    val source_url: String?,
    val video_ids: String?
)

