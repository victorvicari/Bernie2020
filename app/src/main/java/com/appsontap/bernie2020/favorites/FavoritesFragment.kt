package com.appsontap.bernie2020.favorites

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.legislation.LegislationViewHolder
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.models.Quote
import com.appsontap.bernie2020.models.SimpleCategory
import com.appsontap.bernie2020.plans.PlanViewHolder
import com.appsontap.bernie2020.plans.PlansFragment
import com.appsontap.bernie2020.plans.PlansViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_plans.*
import java.lang.RuntimeException

class FavoritesFragment : BaseFragment() {

    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
    }
    private lateinit var data: List<Any>
    private lateinit var favorites: Set<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.title_favorites)
        favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
        viewModel.fetchPlanData()
        // viewModel.fetchLegislationData() apparently this just overwrites the values rather than adds to them
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel
                .dataEmitter
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        data = it
                        recycler_view.adapter =
                            FavoritesAdapter(requireContext(), it)
                    },
                    onError = {
                        Log.e(TAG, "Couldn't get list of plans ${it.message}", it)
                    }
                ).into(bin)
        }
    }

    inner class FavoritesAdapter(var context: Context, var favoriteItems: List<Any>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when(viewType) {
                R.layout.item_plan -> return PlanViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_plan,
                        parent,
                        false
                    )
                )
                R.layout.item_legislation -> return LegislationViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_legislation,
                        parent,
                        false
                    )
                )
            }
            throw RuntimeException("Invalid view type")
        }
        override fun getItemViewType(position: Int): Int {
            when(favoriteItems?.get(position)){
                is Quote -> return R.layout.quote_view_holder
                is Legislation -> return R.layout.item_legislation
                is Plan -> return R.layout.item_plan
            }
            return R.layout.item_generic
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when(holder) {
                is PlanViewHolder -> {
                    when (val item = favoriteItems?.get(position)) {
                        is Plan -> {
                            holder.setTextViewName(item.name)
                            context?.let { holder.setOnClickListener(it, item) }
                            context?.let { holder.setupFavoriteCheckbox(it, item.id, favorites) }
                        }
                    }
                }
                is LegislationViewHolder -> {
                    when (val item = favoriteItems?.get(position)) {
                        is Legislation -> context?.let { holder.bind(item, it, IOHelper.loadFavoritesFromSharedPrefs(it)) }
                    }
                }
            }
        }


        override fun getItemCount(): Int {
            return favoriteItems?.size ?: 0
        }

    }

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

}

