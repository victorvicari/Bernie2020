package com.appsontap.bernie2020.models

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    fun getCategoryIds(): List<String>? {
        category_ids?.let {
            return it.split(" ")
        }
        return null
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category_ids)
        parcel.writeString(created_at)
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeString(legislation_ids)
        parcel.writeString(links)
        parcel.writeString(name)
        parcel.writeString(quote_ids)
        parcel.writeString(video_ids)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Plan> {
        override fun createFromParcel(parcel: Parcel): Plan {
            return Plan(parcel)
        }

        override fun newArray(size: Int): Array<Plan?> {
            return arrayOfNulls(size)
        }
    }
}