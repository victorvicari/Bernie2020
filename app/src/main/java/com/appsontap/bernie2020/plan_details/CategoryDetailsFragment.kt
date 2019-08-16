package com.appsontap.bernie2020.plan_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.legislation.LegislationViewHolder
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.models.Quote
import com.appsontap.bernie2020.plans.PlanViewHolder
import com.appsontap.bernie2020.util.IOHelper
import com.appsontap.bernie2020.util.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_plan_details.*
import kotlinx.android.synthetic.main.header_view_holder.view.*
import kotlinx.android.synthetic.main.item_generic.view.*
import kotlinx.android.synthetic.main.quote_view_holder.view.*
import kotlinx.android.synthetic.main.title_view_holder.view.*

/**
 * Feel the Bern
 */
class CategoryDetailsFragment : BaseFragment() {
    private var categoryId: String? = null
    private var planId: String? = null
    lateinit var favorites: Set<String>
    private val viewModel: CategoryDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(CategoryDetailsViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
        arguments?.let {
            it.run {
                categoryId = getString(EXTRA_CATEGORY_ID)
                planId = getString(EXTRA_PLAN_ID)
            }
        }
        title = getString(R.string.plans_and_proposals)
        Log.d(
            TAG,
            "LOOK AT BACKSTACK COUNT: " + (activity as AppCompatActivity).supportFragmentManager.backStackEntryCount.toString()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plan_details, container, false)
    }

    override fun onStart() {
        super.onStart()
        categoryId?.let {
            viewModel.categoryDetails(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { uiState ->
                        recycler_view.adapter = ProposalDetailAdapter(uiState)
                    },
                    onError = { error ->
                        Log.e(TAG, error.message, error)
                    }
                )
        }

        planId?.let {
            viewModel.planDetails(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { uiState ->
                        recycler_view.adapter = ProposalDetailAdapter(uiState)
                    },
                    onError = { error ->
                        Log.e(TAG, error.message, error)
                    }
                )
        }
    }


    inner class ProposalDetailAdapter(private val uiState: UiState.ListReady) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when (viewType) {
                R.layout.header_view_holder -> return HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.header_view_holder,
                        parent,
                        false
                    )
                )
                R.layout.title_view_holder -> return TitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.title_view_holder,
                        parent,
                        false
                    )
                )
                R.layout.item_legislation -> return LegislationViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_legislation,
                        parent,
                        false
                    ), uiState)
                // temporary placeholder for non-specific items
                R.layout.item_generic -> return ItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_generic,
                        parent,
                        false
                    )
                )
                R.layout.quote_view_holder -> return QuoteViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.quote_view_holder,
                        parent,
                        false
                    )
                )
                R.layout.item_plan -> return PlanViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_plan,
                        parent,
                        false
                    )
                )
            }

            throw RuntimeException("Invalid view type")
        }

        override fun getItemCount(): Int {
            return uiState.items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is HeaderViewHolder -> holder.bind(uiState.items[position] as String)
                is TitleViewHolder -> holder.bind(uiState.items[position] as String)
                is ItemViewHolder -> {
                    //this is just an easy way to cast all these things
                    when (val item = uiState.items[position]) {
                        is Category -> holder.bind(item.name)
                    }
                }
                is LegislationViewHolder -> {
                    when (val item = uiState.items[position]) {
                        is Legislation -> holder.bind(item, IOHelper.loadFavoritesFromSharedPrefs(requireContext()))
                    }
                }
                is QuoteViewHolder -> {
                    when (val item = uiState.items[position]) {
                        is Quote -> holder.bind(item.quote)
                    }
                }
                is PlanViewHolder -> {
                    when (val item = uiState.items[position]) {
                        is Plan -> {
                            holder.setTextViewName(item.name)
                             holder.setOnClickListener(requireContext(), item) 
                             holder.setupFavoriteCheckbox(requireContext(), item.id, favorites)
                        }
                    }
                }

            }
        }

        override fun getItemViewType(position: Int): Int {
            if (position == 0) return R.layout.header_view_holder
            if (uiState.titleIndexes.contains(position)) {
                return R.layout.title_view_holder
            }

            when(uiState.items[position]){
                is Quote -> return R.layout.quote_view_holder
                is Legislation -> return R.layout.item_legislation
                is Plan -> return R.layout.item_plan
            }
            return R.layout.item_generic
        }


        open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        inner class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView) {
            fun bind(title: String?) {
                title.let {
                    itemView.header_title_text_view.text = it
                }
            }
        }

        inner class TitleViewHolder(itemView: View) : BaseViewHolder(itemView) {
            fun bind(title: String?) {
                title.let {
                    itemView.category_title.text = it
                }
            }
        }

        inner class ItemViewHolder(itemView: View) : BaseViewHolder(itemView) {
            fun bind(description: String?){
                description?.let { 
                    itemView.item_name.text = it
                }
            }
        }

        inner class QuoteViewHolder(itemView: View) : BaseViewHolder(itemView) {
            fun bind(quote: String?) {
                itemView.quote_text.text = quote
            }
        }
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_PLAN_ID = "extra_plan_id"
        fun newInstance(args: Bundle): CategoryDetailsFragment {
            return CategoryDetailsFragment().apply { 
                this.arguments = args
            }

        }
    }
}