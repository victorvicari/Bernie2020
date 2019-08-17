package com.appsontap.bernie2020

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_empty_list.view.*

class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    fun bind(message: String, context: Context){
        itemView.textview_empty_list.text = message
    }
}