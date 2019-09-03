package com.appsontap.bernie2020.plan_details


import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.util.TAG
import kotlinx.android.synthetic.main.item_legislation.view.*

class CategoryViewHolder(itemView: View, private val uiState: UiState.ListReady) : RecyclerView.ViewHolder(itemView){

    init {
        itemView.setOnClickListener {
            if (uiState.items[adapterPosition] is Category) {
                val category = uiState.items[adapterPosition] as Category
                val args = Bundle()
                args.putString(CategoryDetailsFragment.EXTRA_CATEGORY_ID, category.id)
                (itemView.context as FragmentActivity)
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        CategoryDetailsFragment.newInstance(args),
                        CategoryDetailsFragment.TAG
                    )
                    .addToBackStack(CategoryDetailsFragment.TAG)
                    .commit()
            }
        }
    }
    
    fun bind(name: String){
        itemView.textview_name.text = name
    }
}