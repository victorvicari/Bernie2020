package com.appsontap.bernie2020.plans

import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.appsontap.bernie2020.models.SimpleCategory
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_plan_category.view.*

class CategoryViewHolder(val categoryView: View) : GroupViewHolder(categoryView) {

    fun setCategoryName(category: ExpandableGroup<*>) {
        if (category is SimpleCategory) {
            categoryView.textview_proposal_category_item_name.setText(category.name)
        }
    }

    override fun expand() {
        animateExpand()
    }

    override fun collapse() {
        animateCollapse()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(360f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        categoryView.imageview_proposal_category_item_arrow.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        categoryView.imageview_proposal_category_item_arrow.animation = rotate
    }
}