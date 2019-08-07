package com.appsontap.bernie2020.legislation

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.IOHelper
import com.appsontap.bernie2020.TAG
import com.appsontap.bernie2020.models.Legislation
import kotlinx.android.synthetic.main.item_legislation.view.*

class LegislationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    init{
        itemView.setOnClickListener {

        }
    }
    fun bind(legislation: Legislation, context: Context, favorites: Set<String>){
        itemView.textview_legislation_name.text = legislation.name
        itemView.checkbox_legislation_favorite?.isChecked = favorites.contains(legislation.id)

        itemView.checkbox_legislation_favorite?.setOnClickListener {
            if(itemView.checkbox_legislation_favorite.isChecked) {
                IOHelper.addFavoriteToSharedPrefs(context, legislation.id)
            } else {
                IOHelper.removeFavoriteFromSharedPrefs(context, legislation.id)
            }
            Log.d(TAG, "FAVORITES: $favorites")
        }
    }
}