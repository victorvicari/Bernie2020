package com.appsontap.bernie2020.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "legislation_table")
data class Legislation(
    val category_ids: String?,
    val congress: Int?,
    val congress_id: String?,
    val created_at: String?,
    val description: String?,
    val whitepaper: String?,
    @PrimaryKey val id: String,
    val links: String?,
    val name: String?,
    val proposal_ids: String?,
    val quote_ids: String?,
    val type: String?,
    val url: String?,
    val video_ids: String?,
    val detail_ids: String?
)