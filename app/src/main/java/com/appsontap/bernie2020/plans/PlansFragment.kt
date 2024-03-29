package com.appsontap.bernie2020.plans

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.utils.MiscUtils
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.models.SimpleCategory
import com.appsontap.bernie2020.plan_details.CategoryDetailsFragment
import com.appsontap.bernie2020.util.IOHelper
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.util.into
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableList
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_legislation.*
import kotlinx.android.synthetic.main.fragment_plans.*
import kotlinx.android.synthetic.main.fragment_plans.recycler_view
import kotlinx.android.synthetic.main.fragment_plans.textview_empty_list
import kotlinx.android.synthetic.main.item_plan_category.view.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*


class PlansFragment : BaseFragment() {

    private lateinit var mListState: Parcelable
    private var mBundleRecyclerViewState = Bundle()
    private val viewModel: PlansViewModel by lazy {
        ViewModelProviders.of(this).get(PlansViewModel::class.java)
    }
    private lateinit var data: List<Any>
    private var simpleCategories: List<SimpleCategory>? = null
    private lateinit var favorites: Set<String>
    private var expandListener : MenuItem.OnActionExpandListener? = null
    private var queryTextListener : SearchView.OnQueryTextListener? = null
    private var clearListener : View.OnClickListener? = null
    private var searchClickListener : View.OnClickListener? = null
    private var isSearchExpanded = false
    private var searchText = ""
    private lateinit var optionsMenu : Menu



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.title_plans)
        viewModel.fetchData()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(
            TAG,
            "LOOK AT BACKSTACK COUNT: " + (activity as AppCompatActivity).supportFragmentManager.backStackEntryCount.toString()
        )

        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.fragment_title_plans)
        favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
        Log.d(TAG, "FAVORITES ARE: $favorites")


            viewModel
                .dataReady()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        data = it
                        // simpleCategories = getSimpleCategoriesFromCategoriesAndPlans(it)
                        simpleCategories = getSimpleCategoriesFromAllItems(it)
                        Log.d(TAG, simpleCategories.toString())
                        simpleCategories?.let{ cats-> recycler_view.adapter = PlansAdapter(requireContext(), cats)}

                        // populate search and filter results if there were a search before leaving fragment
                        val lastSearch = IOHelper.loadPlansSearchStateFromSharedPrefs(requireContext())
                        lastSearch?.let { search ->
                            if(search.length > 0) {
                                filterResults(search)
                                optionsMenu.findItem(R.id.action_search).expandActionView()
                                val searchView = (optionsMenu.findItem(R.id.action_search).actionView as SearchView)
                                searchView.onActionViewExpanded()
                                searchView.findViewById<EditText>(R.id.search_src_text).setText(search)
                                isSearchExpanded = true

                            }
                        }

                        val sharedPref = requireContext().getSharedPreferences("EXPANDED STATE", Context.MODE_PRIVATE)
                        var states: BooleanArray? = null
                        try {
                            val jsonArray = JSONArray(sharedPref.getString("ITEMS", "[]"))
                            states = BooleanArray(jsonArray.length())
                            for (i in 0 until jsonArray.length()) {
                                states[i] = jsonArray.getBoolean(i)
                            }
                            (recycler_view.adapter as PlansAdapter).restoreExpandedState(states)
                            (recycler_view.layoutManager as LinearLayoutManager).scrollToPosition(
                                IOHelper.loadPlansScrollStateFromSharedPrefs(context)
                            )
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    },
                    onError = {
                        Log.e(TAG, "Couldn't get list of plans ${it.message}", it)
                    }
                ).into(bin)


    }

    private fun getSimpleCategoriesFromAllItems(catsAndPlans: List<Any>): List<SimpleCategory> {
        // retrieve all category objects from the list of mixed categories and plans
        val categories = (catsAndPlans.filter {
            it is Category
        } as List<Category>).toSet().sortedBy { it -> it.id.substring(1).toInt() }

        // from each category, create a SimpleCategory object that contains the name of the category
        // and the list of plans that are associated with said category.
        val simpCategories = mutableListOf<SimpleCategory>()
        for(category in categories) {
            simpCategories.add(SimpleCategory(category.name,
                catsAndPlans.filter {
                    it is Plan && it.category_ids?.split(" ")!!.contains(category.id)
            } as MutableList<Plan>, category.id))
        }
        Log.d(TAG, simpCategories.toString())
        return simpCategories
    }

    private fun getSimpleCategoriesByKeyword(catsAndPlans: List<Any>, keyword: String) : MutableList<SimpleCategory> {
        val locale = Locale.getDefault()
        val lowerCaseKeyword = keyword.toLowerCase(locale)
        val categories = (catsAndPlans.filter {
            it is Category
        } as List<Category>).toSet().sortedBy { it -> it.id.substring(1).toInt() }

        val simpCategories = mutableListOf<SimpleCategory>()
        for(category in categories) {
            simpCategories.add(SimpleCategory(category.name,
                catsAndPlans.filter {
                    it is Plan
                        && it.category_ids?.split(" ")?.contains(category.id) ?: false
                        && (it.name?.toLowerCase(locale)?.contains(lowerCaseKeyword) ?: false
                            || it.description?.toLowerCase(locale)?.contains(lowerCaseKeyword) ?: false)
                } as MutableList<Plan>, category.id))
        }
        Log.d(TAG, simpCategories.toString())
        return simpCategories
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bin.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.options_menu_plans, menu)
        if (menu != null) {
            optionsMenu = menu
        }
        // Associate searchable configuration with the SearchView
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(activity!!.componentName)
        )

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val filteredResults = searchDataByKeyword(searchView.query.toString())
                recycler_view.adapter = PlansAdapter(requireContext(), filteredResults)
                (recycler_view.adapter as PlansAdapter).expandAll()
                recycler_view.adapter?.notifyDataSetChanged()

                val expandCollapseMenuItem = menu.findItem(R.id.action_expand_collapse_all)
                expandCollapseMenuItem.setTitle(R.string.plans_collapse_all)
                expandCollapseMenuItem.setIcon(R.drawable.ic_unfold_less_white_24dp)

                textview_empty_list.visibility =
                    (if (recycler_view.adapter?.itemCount == 0) View.VISIBLE else View.GONE)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchText = newText
                simpleCategories?.let {
                    if (newText.isEmpty() && recycler_view != null) {
                        recycler_view.adapter = PlansAdapter(requireContext(), it)
                        recycler_view.adapter?.notifyDataSetChanged()
                        textview_empty_list.visibility =
                            (if (recycler_view.adapter?.itemCount == 0) View.VISIBLE else View.GONE)
                    }
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
                simpleCategories?.let {
                    recycler_view.adapter =  PlansAdapter(requireContext(), it)
                    recycler_view.adapter?.notifyDataSetChanged()

                    val expandCollapseMenuItem = menu.findItem(R.id.action_expand_collapse_all)
                    expandCollapseMenuItem.setTitle(R.string.plans_expand_all)
                    expandCollapseMenuItem.setIcon(R.drawable.ic_unfold_more_white_24dp)
                    isSearchExpanded = false
                }
                return true
            }
        }
        menu.findItem(R.id.action_search).setOnActionExpandListener(expandListener)


        // listens for the press of the x button and collapses the searchview
        clearListener = object : View.OnClickListener {
            override fun onClick(clearButton : View?) {
                val expandCollapseMenuItem = menu.findItem(R.id.action_expand_collapse_all)
                expandCollapseMenuItem.setTitle(R.string.plans_expand_all)
                expandCollapseMenuItem.setIcon(R.drawable.ic_unfold_more_white_24dp)
                searchView.findViewById<EditText>(R.id.search_src_text).setText("")
                searchView.setQuery("", false)
                searchView.onActionViewCollapsed()
                menu.findItem(R.id.action_search).collapseActionView()
                isSearchExpanded = false
            }
        }
        searchView.findViewById<ImageView>(R.id.search_close_btn).setOnClickListener(clearListener)

        // manually listen for the search button click to allow tracking of search state
        searchClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                menu.findItem(R.id.action_search).expandActionView()
                searchView.onActionViewExpanded()
                isSearchExpanded = true
            }
        }
        searchView.setOnSearchClickListener(searchClickListener)
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_expand_collapse_all -> {
                val adapter = (recycler_view.adapter as PlansAdapter)
                if(adapter.isFullyExpanded() || item.title == getString(R.string.plans_collapse_all)) {
                    adapter.collapseAll()
                    item.setIcon(R.drawable.ic_unfold_more_white_24dp)
                    item.setTitle(getString(R.string.plans_expand_all))
                } else {
                    adapter.expandAll()
                    item.setTitle(getString(R.string.plans_collapse_all))
                    item.setIcon(R.drawable.ic_unfold_less_white_24dp)
                }
            }
        }
        return true
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        expandListener = null
        queryTextListener = null
        clearListener = null
        searchClickListener = null
    }


    private fun searchDataByKeyword(keyword: String): List<SimpleCategory> {
        val categories = getSimpleCategoriesByKeyword(data, keyword)
        // check if the category is empty and remove it
        for (i in (categories.size - 1) downTo 0) {
            if (categories[i].plans.isEmpty()) {
                categories.removeAt(i)
            }
        }
        return categories
    }

    internal inner class PlansAdapter(val context: Context, val data: List<SimpleCategory>) :
        ExpandableRecyclerViewAdapter<CategoryViewHolder, PlanViewHolder>(data) {

        override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_plan_category, parent, false)
            return CategoryViewHolder(view)
        }

        override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): PlanViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_plan, parent, false)
            return PlanViewHolder(view)
        }

        override fun onBindChildViewHolder(
            holder: PlanViewHolder?,
            flatPosition: Int,
            group: ExpandableGroup<*>?,
            childIndex: Int
        ) {
            val proposal = (group as SimpleCategory).items[childIndex]
            holder?.setTextViewName(proposal.name)
            holder?.setOnClickListener(context, proposal)
            holder?.setupFavoriteCheckbox(context, proposal.id, favorites)
            holder?.setShareClickListener(requireActivity(), proposal)
            favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
        }


        override fun onBindGroupViewHolder(holder: CategoryViewHolder?, flatPosition: Int, group: ExpandableGroup<*>) {
            holder?.setCategoryName(group)
            holder?.categoryView?.textview_proposal_category_item_name?.setOnClickListener {
                val args = Bundle()
                args.putString(CategoryDetailsFragment.EXTRA_CATEGORY_ID, (group as SimpleCategory).id)
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        CategoryDetailsFragment.newInstance(args),
                        CategoryDetailsFragment.TAG
                    )
                    .addToBackStack(CategoryDetailsFragment.TAG)
                    .commit()
            }

            if (isGroupExpanded(group)) {
                holder?.expand()
            }
        }

        fun isFullyExpanded() : Boolean {
            for(i in groups.size-1 downTo 0) {
                if(!isGroupExpanded(groups[i])) {
                    return false
                }
            }
            return true;
        }

        fun expandAll() {
            for(i in groups.size-1 downTo 0) {
                if(!isGroupExpanded(groups[i])) {
                    toggleGroup(groups[i])
                }
            }
            notifyDataSetChanged()
        }

        fun collapseAll() {
            for(i in groups.size-1 downTo 0) {
                if(isGroupExpanded(groups[i])) {
                    toggleGroup(groups[i])
                }
            }
            notifyDataSetChanged()
        }

        fun getExpandedState(): BooleanArray {
            val expandedState = BooleanArray(groups.size)
            var stateIndex = 0
            for (i in 0 until itemCount) {
                if (stateIndex < expandedState.size && getItemViewType(i) == 2) {
                    expandedState[stateIndex] = isGroupExpanded(i)
                    stateIndex++
                }
            }
            return expandedState
        }

        fun restoreExpandedState(expandedState: BooleanArray) {
            Log.d(TAG, "restoreExpandedState: " + Arrays.toString(expandedState))
            var stateIndex = 0
            for (i in 0 until itemCount) {
                if (getItemViewType(i) == 2) {
                    if (stateIndex < expandedState.size && expandedState[stateIndex]) {
                        toggleGroup(i)
                    }
                    stateIndex++
                }
            }
        }

        override fun onGroupExpanded(positionStart: Int, itemCount: Int) {
            super.onGroupExpanded(positionStart, itemCount)
            saveState()
        }

        override fun onGroupCollapsed(positionStart: Int, itemCount: Int) {
            super.onGroupCollapsed(positionStart, itemCount)
            saveState()
        }

        fun saveState() {
            val sharedPref =
                requireContext().getSharedPreferences("EXPANDED STATE", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val jsonArray = JSONArray()
            val states = getExpandedState()
            for (b in states) {
                jsonArray.put(b)
            }
            editor.putString("ITEMS", jsonArray.toString())
            editor.apply()
        }

    }

    override fun onPause() {
        super.onPause()
        val lastFirstVisiblePosition =
            (recycler_view.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        IOHelper.savePlansScrollStateToSharedPrefs(context, lastFirstVisiblePosition)
        if(isSearchExpanded) {
            IOHelper.savePlansSearchStateToSharedPrefs(context, searchText)
        } else {
            IOHelper.savePlansSearchStateToSharedPrefs(context, "")
        }

    }

    fun filterResults(query : String) {
        val filteredResults = searchDataByKeyword(query)
        recycler_view.adapter = PlansAdapter(requireContext(), filteredResults)
        (recycler_view.adapter as PlansAdapter).expandAll()
        recycler_view.adapter?.notifyDataSetChanged()

        val expandCollapseMenuItem = optionsMenu.findItem(R.id.action_expand_collapse_all)
        expandCollapseMenuItem.setTitle(R.string.plans_collapse_all)
        expandCollapseMenuItem.setIcon(R.drawable.ic_unfold_less_white_24dp)

        textview_empty_list.visibility =
            (if (recycler_view.adapter?.itemCount == 0) View.VISIBLE else View.GONE)
    }

    companion object {
        fun newInstance(): PlansFragment {
            return PlansFragment()
        }
    }

}