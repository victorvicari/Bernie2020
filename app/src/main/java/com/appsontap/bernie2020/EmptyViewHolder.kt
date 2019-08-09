package com.appsontap.bernie2020

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.models.Legislation
import kotlinx.android.synthetic.main.item_empty_list.view.*
import kotlinx.android.synthetic.main.item_legislation.view.*

class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bind(message: String, context: Context){
        itemView.textview_empty_list.text = message
    }
}