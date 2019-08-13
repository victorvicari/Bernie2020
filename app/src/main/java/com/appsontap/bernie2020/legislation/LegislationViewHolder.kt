package com.appsontap.bernie2020.legislation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.legislation_details.LegislationDetailsFragment
import com.appsontap.bernie2020.util.IOHelper
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.plan_details.UiState
import kotlinx.android.synthetic.main.item_legislation.view.*

class LegislationViewHolder(itemView: View, private val uiState: UiState.ListReady) : RecyclerView.ViewHolder(itemView){

    init {
        itemView.setOnClickListener {
            if (uiState.items[adapterPosition] is Legislation) {
                val legislation = uiState.items[adapterPosition] as Legislation
                val args = Bundle()
                args.putParcelable(LegislationDetailsFragment.EXTRA_KEY_LEGISLATION, legislation)
                (itemView.context as FragmentActivity)
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        LegislationDetailsFragment.newInstance(args),
                        LegislationDetailsFragment.TAG
                    )
                    .addToBackStack(LegislationDetailsFragment.TAG)
                    .commit()
            }
        }
    }
    
    fun bind(legislation: Legislation, favorites: Set<String>){
        itemView.textview_legislation_name.text = legislation.name
        itemView.checkbox_legislation_favorite?.isChecked = favorites.contains(legislation.id)

        itemView.checkbox_legislation_favorite?.setOnClickListener {
            if(itemView.checkbox_legislation_favorite.isChecked) {
                IOHelper.addFavoriteToSharedPrefs(itemView.context, legislation.id)
            } else {
                IOHelper.removeFavoriteFromSharedPrefs(itemView.context, legislation.id)
            }
            Log.d(TAG, "FAVORITES: $favorites")
        }
    }
}