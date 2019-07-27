package com.appsontap.bernie2020.timeline

import com.appsontap.bernie2020.models.TimelineItem
import java.lang.RuntimeException

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class Timeline {
    var sections = mutableListOf<Section>()
    val flattenedList = mutableListOf<Any>()

    fun insert(section: Section) {
        sections.add(section)
    }

    fun contains(year: String): Boolean {
        sections.forEach {
            if (it.year == year) return true
        }
        return false
    }

    fun getSectionForYear(year: String): Section? {
        sections.forEach { 
            if(it.year == year) return it
        }
        return null
    }

    fun updateSection() {

    }

    fun totalItemCount() : Int {
        var count = 0
        sections.forEach {
            count +=1
            count +=it.itemsForYear.size
        }
        return count
    }
    
    fun getTypeForPosition(position: Int): ViewType {
        flatten()
        val item = flattenedList[position]
         when(item){
             is String -> return ViewType.YEAR
             is TimelineItem -> {
                 item.image_url?.let { 
                     return ViewType.IMAGE
                 }
                 return ViewType.TEXT
             }
         }
        
        throw RuntimeException("Invalid item type")
    }
    
    //todo don't return Any return an enum or something
    fun getItemAtPosition(position: Int): Any {
        flatten()
        return flattenedList[position]
    }
    
    private fun flatten(){
        if(flattenedList.isEmpty()) {
            sections.forEach {
                flattenedList.add(it.year)
                it.itemsForYear.forEach { timelineItem ->
                    flattenedList.add(timelineItem)
                }
            }
        }
    }

    inner class Section(val year: String) {
        val itemsForYear = mutableListOf<TimelineItem>()
    }
}
