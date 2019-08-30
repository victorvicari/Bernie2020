package com.appsontap.bernie2020.legislation

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.plan_details.UiState
import com.appsontap.bernie2020.util.IOHelper
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.util.into
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_legislation.*
import kotlinx.android.synthetic.main.fragment_legislation.recycler_view


/**
 * Feel the Bern
 */
class LegislationFragment : BaseFragment() {
    private val viewModel: LegislationViewModel by lazy {
        ViewModelProviders.of(this).get(LegislationViewModel::class.java)
    }
    private lateinit var favorites: Set<String>
    private lateinit var uiState: UiState.ListReady
    private var expandListener : MenuItem.OnActionExpandListener? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.drawer_legislation)
        favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_legislation, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel
            .dataEmitter
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { uiState ->
                    when (uiState) {
                        is UiState.ListReady -> {
                            this.uiState = uiState
                            @Suppress("UNCHECKED_CAST")
                            recycler_view.adapter = LegislationAdapter(uiState.items as List<Legislation>)
                        }
                    }
                },
                onError = {
                    Log.e(TAG, "${it.message}", it)
                }
            ).into(bin)
    }

    override fun onStop() {
        super.onStop()
        bin.clear()
    }

    companion object {
        fun newInstance(): LegislationFragment {
            return LegislationFragment()
        }
    }

    inner class LegislationAdapter(val items: List<Legislation>) : RecyclerView.Adapter<LegislationViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegislationViewHolder {
            return LegislationViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_legislation,
                    parent,
                    false
                ), uiState)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: LegislationViewHolder, position: Int) {
            holder.bind(items[position], IOHelper.loadFavoritesFromSharedPrefs(requireContext()))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.options_menu_searchable, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(activity!!.componentName)
        )
        
        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val filteredResults = searchDataByKeyword(searchView.query.toString())
                recycler_view.adapter = LegislationAdapter(filteredResults)
                recycler_view.adapter?.notifyDataSetChanged()
                textview_empty_list.visibility =
                    (if (recycler_view.adapter?.itemCount == 0) View.VISIBLE else View.GONE)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty() && recycler_view != null) {
                    recycler_view.adapter = LegislationAdapter(uiState.items as List<Legislation>)
                    recycler_view.adapter?.notifyDataSetChanged()
                    textview_empty_list.visibility =
                        (if (recycler_view.adapter?.itemCount == 0) View.VISIBLE else View.GONE)
                }
                return true
            }
        }
        
        searchView.setOnQueryTextListener(queryTextListener)
        // listens for the back button press to reset the adapter to the full list
        expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                recycler_view.adapter = LegislationAdapter(uiState.items as List<Legislation>)
                recycler_view.adapter?.notifyDataSetChanged()
                return true
            }
        }
        menu.findItem(R.id.action_search).setOnActionExpandListener(expandListener)
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        expandListener = null
        queryTextListener = null
        
    }

    private fun searchDataByKeyword(keyword: String): List<Legislation> {
        val filteredResults = mutableListOf<Legislation>()
        for (item in uiState.items) {
            when (item) {
                is Legislation -> {
                    if (item.name?.contains(keyword, true) == true || item.description?.contains(
                            keyword,
                            true
                        ) == true
                    ) {
                        filteredResults.add(item)
                    }
                }
            }
        }
        return filteredResults
    }
}