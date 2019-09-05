package com.appsontap.bernie2020.legislation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.legislation_details.LegislationDetailsFragment
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.plan_details.UiState
import com.appsontap.bernie2020.util.IOHelper
import com.appsontap.bernie2020.util.TAG
import kotlinx.android.synthetic.main.item_legislation.view.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity


class LegislationViewHolder(itemView: View, private val uiState: UiState.ListReady) :
    RecyclerView.ViewHolder(itemView) {

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
        if(adapterPosition >= 0 && adapterPosition < uiState.items.size && uiState.items[adapterPosition] is Legislation) {
            val legislation = uiState.items[adapterPosition] as Legislation
            itemView.checkbox_favorite?.setOnClickListener {
                if (itemView.checkbox_favorite.isChecked) {
                    IOHelper.addFavoriteToSharedPrefs(itemView.context, legislation.id)
                } else {
                    IOHelper.removeFavoriteFromSharedPrefs(itemView.context, legislation.id)
                }
            }
        }

        itemView.imageview_share?.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, (uiState.items[adapterPosition] as Legislation).name)
                type = "text/plain"
            }
            itemView.context.startActivity(Intent.createChooser(sendIntent, "Placeholder Text"))
        }
    }

    fun bind(legislation: Legislation, favorites: Set<String>) {
        itemView.textview_name.text = legislation.name
        itemView.checkbox_favorite?.isChecked = favorites.contains(legislation.id)
    }
}