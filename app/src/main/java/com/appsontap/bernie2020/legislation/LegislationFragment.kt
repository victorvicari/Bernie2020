package com.appsontap.bernie2020.legislation

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.models.Legislation
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
    val repo = LegislationRepo()
    private lateinit var favorites: Set<String>
    private lateinit var data: List<Legislation>


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
        repo
            .getLegislation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { legislation ->
                    recycler_view.adapter = LegislationAdapter(legislation)
                    data = legislation
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
                )
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: LegislationViewHolder, position: Int) {
            context?.let { holder.bind(items[position], it, IOHelper.loadFavoritesFromSharedPrefs(it)) }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.options_menu_searchable, menu)
        // Associate searchable configuration with the SearchView
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(activity!!.componentName)
        )
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val filteredResults = searchDataByKeyword(searchView.query.toString())
                recycler_view.adapter = LegislationAdapter(filteredResults)
                recycler_view.adapter?.notifyDataSetChanged()
                textview_empty_list.visibility =
                    (if (recycler_view.adapter?.itemCount == 0) View.VISIBLE else View.GONE)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length == 0 && recycler_view != null) {
                    recycler_view.adapter =LegislationAdapter(data)
                    recycler_view.adapter?.notifyDataSetChanged()
                    textview_empty_list.visibility =
                        (if (recycler_view.adapter?.itemCount == 0) View.VISIBLE else View.GONE)
                }
                return true
            }
        })
        // listens for the back button press to reset the adapter to the full list
        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                recycler_view.adapter = LegislationAdapter(data)
                recycler_view.adapter?.notifyDataSetChanged()
                return true
            }
        }
        menu.findItem(R.id.action_search).setOnActionExpandListener(expandListener)
    }

    private fun searchDataByKeyword(keyword: String): List<Legislation> {
        val filteredResults = mutableListOf<Legislation>()
        for (item in data) {
            if (item.name?.contains(keyword, true) == true || item.description?.contains(keyword, true) == true) {
                filteredResults.add(item)
            }
        }
        return filteredResults
    }
}