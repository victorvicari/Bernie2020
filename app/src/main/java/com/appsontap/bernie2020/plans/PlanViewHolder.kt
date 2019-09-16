package com.appsontap.bernie2020.plans

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import com.appsontap.bernie2020.util.IOHelper
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.plan_details.CategoryDetailsFragment
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import kotlinx.android.synthetic.main.item_plan.view.*

class PlanViewHolder(private val planView: View) : ChildViewHolder(planView) {
    fun setTextViewName(name: String?) {
        planView.textview_proposal_item_name.text = name
    }

    fun setOnClickListener
                (context: Context, plan: Plan?) {
        planView.textview_proposal_item_name.setOnClickListener {
            val args = Bundle()
            args.putString(CategoryDetailsFragment.EXTRA_PLAN_ID, plan?.id)

            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    CategoryDetailsFragment.newInstance(args),
                    CategoryDetailsFragment.TAG
                )
                .addToBackStack(CategoryDetailsFragment.TAG)
                .commit()
        }
    }

    fun setShareClickListener(context: Context, plan: Plan?) {
        planView.imageview_plan_share.setOnClickListener {
            val popup = PopupMenu(context, planView.imageview_plan_share)
            popup.inflate(R.menu.context_menu_share_plan)
            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.context_plan_share_twitter -> {
                        Log.d(TAG, "twitter twittering!")

                        return@setOnMenuItemClickListener true
                    }
                    R.id.context_plan_share_full_text -> {
                        Log.d(TAG, "full text incoming!")
                        return@setOnMenuItemClickListener true
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }
    }

    fun setupFavoriteCheckbox(context: Context, id: String, favorites: Set<String>) {
        planView.checkbox_plan_favorite.isChecked = favorites.contains(id)

        planView.checkbox_plan_favorite.setOnClickListener {
            if(planView.checkbox_plan_favorite.isChecked) {
                IOHelper.addFavoriteToSharedPrefs(context, id)
            } else {
                IOHelper.removeFavoriteFromSharedPrefs(context, id)
            }
        }
    }

}