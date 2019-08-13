package com.appsontap.bernie2020.legislation_details

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonArray

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class MarkupViewModel : ViewModel(){
    fun viewLoaded(markup: String){
       val json = Gson().fromJson(markup, JsonArray::class.java)
        
    }
}