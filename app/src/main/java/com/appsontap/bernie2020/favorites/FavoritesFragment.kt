package com.appsontap.bernie2020.favorites

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.legislation.LegislationViewHolder
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.models.Quote
import com.appsontap.bernie2020.plan_details.UiState
import com.appsontap.bernie2020.plans.PlanViewHolder
import com.appsontap.bernie2020.util.IOHelper
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.util.into
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_plans.*
import kotlinx.android.synthetic.main.fragment_plans.recycler_view
import java.lang.RuntimeException

class FavoritesFragment : BaseFragment() {

    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
    }
    private lateinit var uiState: UiState.ListReady
    private lateinit var favorites: Set<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.title_favorites)
        favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
        viewModel.fetchData()
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
                        uiState = it
                        if(uiState.items.isEmpty()){
                            showEmptyView()
                        }else {
                            recycler_view.adapter =
                                FavoritesAdapter(requireContext(), uiState.items)
                        }
                    },
                    onError = {
                        Log.e(TAG, "Couldn't get list of plans ${it.message}", it)
                    }
                ).into(bin)
        }
    }

    private fun showEmptyView() {
        empty_lottieAnimationView.addAnimatorUpdateListener { valueAnimator ->
            if (add_favorites_textview.visibility == View.INVISIBLE && valueAnimator.isRunning) {
                add_favorites_textview.visibility = View.VISIBLE
            }
        }
        empty_view_container.visibility = View.VISIBLE

        empty_lottieAnimationView.playAnimation()
    }
    
    inner class FavoritesAdapter(var context: Context, private var favoriteItems: List<Any>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                    , uiState
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
                            holder.setOnClickListener(context, item) 
                            holder.setupFavoriteCheckbox(context, item.id, favorites)
                        }
                    }
                }
                is LegislationViewHolder -> {
                    when (val item = favoriteItems?.get(position)) {
                        is Legislation -> holder.bind(item, IOHelper.loadFavoritesFromSharedPrefs(context))
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

