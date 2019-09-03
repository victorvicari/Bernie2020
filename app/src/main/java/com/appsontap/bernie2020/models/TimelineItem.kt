package com.appsontap.bernie2020.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timeline_table")
data class TimelineItem(
    val article_url: String?,
    val category_ids: String?,
    val description: String,
    val explanation_of_action: String?,
    @PrimaryKey val id: String,
    val image_url: String?,
    val legislation_ids: String?,
    val name: String?,
    val proposal_ids: String?,
    val quote_ids: String?,
    val related_links: String?,
    val video_url: String?,
    val year: String,
    val image_resource: String?
)