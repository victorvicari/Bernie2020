package com.appsontap.bernie2020.models

import android.os.Parcel
import android.os.Parcelable
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class SimpleCategory(val name: String, val plans: MutableList<Plan>) : ExpandableGroup<Plan>(name, plans) {
    fun addPlan(plan: Plan) {
        plans.add(plan)
    }

    override fun toString(): String {
        return "SimpleCategory(name='$name', plans=$plans)"
    }

}