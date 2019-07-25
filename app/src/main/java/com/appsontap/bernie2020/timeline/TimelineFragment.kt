package com.appsontap.bernie2020.timeline

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class TimelineFragment : Fragment(){
    
    
    inner class TimelineAdapter : RecyclerView.Adapter<TimelineAdapter.BaseViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemCount(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }

        inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            
        }
    }
    
    companion object {
        fun newInstance(): TimelineFragment {
            return TimelineFragment()
        }
    }
}