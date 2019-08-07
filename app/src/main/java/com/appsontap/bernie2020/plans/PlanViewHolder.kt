package com.appsontap.bernie2020.plans

import android.view.View
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import kotlinx.android.synthetic.main.item_plan.view.*

class PlanViewHolder(val planView: View) : ChildViewHolder(planView) {
    fun setTextViewName(name: String?) {
        planView.textview_proposal_item_name.setText(name)
    }

    fun setTextViewDesc(desc: String?) {
        planView.textview_proposal_item_desc.setText(desc)
    }
}