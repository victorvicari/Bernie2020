package com.appsontap.bernie2020.util

import androidx.room.TypeConverter
import com.google.gson.JsonElement
import com.google.gson.JsonParser

/**
 * These are converters for Room to serialize complex objects to primitives
 */
class Converters{
    @TypeConverter
    fun stringToJsonElement(input : String) : JsonElement{ 
        return JsonParser().parse(input)
    }
    
    @TypeConverter
    fun jsonArrayToString(jsonElement: JsonElement) : String {
        return jsonElement.toString()
    }
}