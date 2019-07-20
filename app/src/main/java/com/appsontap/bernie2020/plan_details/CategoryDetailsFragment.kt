package com.appsontap.bernie2020.plan_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.Constants
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.TAG
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.models.Plan
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.title_view_holder.view.*
import kotlinx.android.synthetic.main.fragment_plan_details.*
import kotlinx.android.synthetic.main.header_view_holder.view.*
import kotlinx.android.synthetic.main.item_view_holder.view.*
import java.lang.RuntimeException

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class CategoryDetailsFragment : Fragment() {
    var categoryId: String? = null
    var proposalName: String? = null
    private val viewModel : CategoryDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(CategoryDetailsViewModel::class.java)
    }
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.run {
                categoryId = getString(EXTRA_CATEGORY_ID)
                proposalName = getString(EXTRA_PROPOSAL_NAME)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plan_details, container, false)
    }

    override fun onStart() {
        super.onStart()
        categoryId?.let { 
                    viewModel.fetchData(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onSuccess = {uiState ->
                                recycler_view.adapter = ProposalDetailAdapter(uiState)
                            },
                            onError = { error ->
                                Log.e(TAG, error.message, error)
                            }
                        )}
        }
    

    inner class ProposalDetailAdapter(val uiState: UiState.ListReady) : RecyclerView.Adapter<ProposalDetailAdapter.BaseViewHolder>() {

   
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            when(viewType){
                R.layout.header_view_holder -> return HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_view_holder, parent, false))
                R.layout.title_view_holder -> return TitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.title_view_holder, parent, false))
                R.layout.item_view_holder -> return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_holder, parent, false))
            }
            
            throw RuntimeException("Invalid view type")
        }

        override fun getItemCount(): Int {
            //1 for header
            return uiState.items.size
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            when(holder){
                is HeaderViewHolder -> holder.bind(uiState.items[position] as String)
                is TitleViewHolder -> holder.bind(uiState.items[position] as String)
                is ItemViewHolder -> {
                    when(val item = uiState.items[position]){
                        is Plan -> holder.bind(item.name)
                        is Legislation -> holder.bind(item.name)
                    }
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            if(position == 0) return R.layout.header_view_holder
            if(uiState.titleIndexes.contains(position)){
                return R.layout.title_view_holder
            }
            return R.layout.item_view_holder
        }
        
        

        open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        inner class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView) {
            fun bind(title : String?){
                title.let { 
                    itemView.header_title_text_view.text = it
                }
            }
        }

        inner class TitleViewHolder(itemView: View) : BaseViewHolder(itemView) {
            fun bind(title: String?){
                title.let { 
                    itemView.category_title.text = it
                }
            }
        }

        inner class ItemViewHolder(itemView: View) : BaseViewHolder(itemView) {
            fun bind(description: String?){
                description?.let { 
                    itemView.plan_text.text = it
                }
            }
        }
    }

    companion object {
        val EXTRA_CATEGORY_ID = "extra_category_id"
        val EXTRA_PROPOSAL_NAME = "extra_proposal_name"
        fun newInstance(args: Bundle): CategoryDetailsFragment {
            val fragment = CategoryDetailsFragment()
            fragment.arguments = args
            return fragment

        }
    }
}