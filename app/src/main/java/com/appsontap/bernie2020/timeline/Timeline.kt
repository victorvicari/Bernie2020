package com.appsontap.bernie2020.timeline

import com.appsontap.bernie2020.models.TimelineItem
import java.time.Year

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class Timeline {
    var sections = mutableListOf<Section>()
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

    inner class Section(val year: String) {
        val itemsForYear = mutableListOf<TimelineItem>()
    }
}
