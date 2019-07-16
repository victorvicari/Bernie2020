package com.appsontap.bernie2020.models

data class Legislation(
    val category_ids: String?,
    val congress: Int?,
    val congress_id: String?,
    val created_at: String?,
    val description: String?,
    val whitepaper: String?,
    val id: String?,
    val links: List<String>?,
    val name: String?,
    val proposal_ids: List<String>?,
    val quote_ids: List<String>?,
    val type: String?,
    val url: String?,
    val video_ids: List<String>?
)