package com.appsontap.bernie2020.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.parcel.Parcelize

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
    val detail_ids: String?,
    var markup: JsonElement
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()!!,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        Gson().fromJson(source.readString(), JsonElement::class.java)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(category_ids)
        writeValue(congress)
        writeString(congress_id)
        writeString(created_at)
        writeString(description)
        writeString(whitepaper)
        writeString(id)
        writeString(links)
        writeString(name)
        writeString(proposal_ids)
        writeString(quote_ids)
        writeString(type)
        writeString(url)
        writeString(video_ids)
        writeString(detail_ids)
        writeString(markup.toString())
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Legislation> = object : Parcelable.Creator<Legislation> {
            override fun createFromParcel(source: Parcel): Legislation = Legislation(source)
            override fun newArray(size: Int): Array<Legislation?> = arrayOfNulls(size)
        }
    }
} 