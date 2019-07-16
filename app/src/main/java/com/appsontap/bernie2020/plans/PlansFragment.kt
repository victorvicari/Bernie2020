package com.appsontap.bernie2020.plans

import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.AppDatabase
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.TAG
import com.appsontap.bernie2020.into
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.models.Plan
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.category_view_holder.view.*
import kotlinx.android.synthetic.main.fragment_plans.*
import kotlinx.android.synthetic.main.plan_view_holder.view.*
import java.lang.RuntimeException

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class PlansFragment : Fragment() {

    val viewModel : PlansViewModel by lazy { 
        ViewModelProviders.of(this).get(PlansViewModel::class.java)
    }
    val bin = CompositeDisposable()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.dataEmitter
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    recycler_view.adapter = PlansAdapter(requireContext(), it)
                },
                onError = {
                    Log.e(TAG, "Couldn't get list of plans ${it.message}", it)
                }
            ).into(bin)
    }

    override fun onStop() {
        super.onStop()
        bin.clear()
    }

    class PlansAdapter(val context: Context, val data: List<Any>) : RecyclerView.Adapter<PlansAdapter.BaseViewHolder>(){
       
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            when(viewType){
                R.layout.category_view_holder -> return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_view_holder, parent, false))
                R.layout.plan_view_holder -> return PlanViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.plan_view_holder, parent, false))
            }
            
            throw RuntimeException("Invalid viewholder type")
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            when(holder){
                is CategoryViewHolder -> {
                    holder.itemView.category_title.text = (data[position] as Category).name
                }
                is PlanViewHolder -> {
                    holder.itemView.plan_text.text = (data[position] as Plan).name
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            when(data[position]){
                is Category -> return R.layout.category_view_holder
                is Plan -> return R.layout.plan_view_holder
            }
            
            throw RuntimeException("Unsupported View Type")
        }
        
        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
        }

        open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            
        }
        
        class CategoryViewHolder(itemView: View) : BaseViewHolder(itemView) {
            
        }
        
        class PlanViewHolder(itemView: View) : BaseViewHolder(itemView) {
            
        }
        
    }
    companion object {
        fun newInstance(): PlansFragment {
            return PlansFragment()
        }
    }
}