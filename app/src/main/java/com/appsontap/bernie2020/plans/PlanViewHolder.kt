package com.appsontap.bernie2020.plans

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import android.content.Intent
import android.view.MotionEvent


class PlanViewHolder(private val planView: View) : ChildViewHolder(planView), View.OnTouchListener {
    init {
        planView.textview_proposal_item_name.setOnTouchListener(this)
    }
    
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

    /**
     * This is needed because the textview has selectable text
     * Without this, it takes 2 taps to tap the textview
     * https://stackoverflow.com/questions/22653641/using-onclick-on-textview-with-selectable-text-how-to-avoid-double-click
     */
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v?.id) {
            R.id.textview_proposal_item_name -> 
                if (event?.action == MotionEvent.ACTION_DOWN) v.requestFocus()
        }
        return false
    }

    fun setShareClickListener(activity: Activity, plan: Plan?) {
        planView.imageview_plan_share.setOnClickListener {
            val popup = PopupMenu(activity, planView.imageview_plan_share)
            popup.inflate(R.menu.context_menu_share_plan_and_leg)
            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.context_share_twitter -> {
                        val message =
                            plan?.let { it1 -> IOHelper.getPlanStringForTwitter(activity, it1) }
                        Log.d(TAG, message.toString())
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = message
                        activity.startActivity(i)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.context_share_full_text -> {
                        var message = activity.getString(R.string.plan_share_full_text_preamble) + plan?.name
                        if(plan?.description != null) {
                            message += "\n\n" + plan?.description
                        }
                        val link = plan?.links?.split(" ")?.get(0)
                        if(link != null && link.isNotEmpty()) {
                            message += activity.getString(R.string.share_full_text_link) + link
                        }
                        val i = Intent(Intent.ACTION_SEND)
                        i.setType("text/plain")
                        i.putExtra(Intent.EXTRA_TEXT, message)
                        activity.startActivity(i)
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