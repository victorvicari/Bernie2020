package com.appsontap.bernie2020.plans

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout.HORIZONTAL
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.models.SimpleCategory
import com.appsontap.bernie2020.plan_details.CategoryDetailsFragment
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_plans.*
import kotlinx.android.synthetic.main.item_plan.view.*
import kotlinx.android.synthetic.main.item_plan_category.view.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import androidx.recyclerview.widget.DividerItemDecoration
import kotlin.collections.HashSet


class PlansFragment : BaseFragment() {

    private val viewModel: PlansViewModel by lazy {
        ViewModelProviders.of(this).get(PlansViewModel::class.java)
    }
    private lateinit var data : List<Any>
    private lateinit var simpleCategories : List<SimpleCategory>
    private lateinit var favorites: Set<String>

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
        Log.d(TAG, "LOOK AT BACKSTACK COUNT: " + (activity as AppCompatActivity).supportFragmentManager.backStackEntryCount.toString())

        (activity as AppCompatActivity).supportActionBar!!.setTitle(getString(R.string.fragment_title_plans))
        favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
        Log.d(TAG, "FAVORITES ARE: "+favorites.toString())

        if(savedInstanceState == null) {
            viewModel
                .dataEmitter
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        data = it
                        simpleCategories = getSimpleCategoriesFromCategoriesAndPlans(it)
                        // TODO rewrite adapters for the expandable recycler views
                        recycler_view.adapter = PlansAdapter(requireContext(), simpleCategories as List<SimpleCategory>)
                    },
                    onError = {
                        Log.e(TAG, "Couldn't get list of plans ${it.message}", it)
                    }
                ).into(bin)
        }
    }
    // data for the expandable recycler view must be provided in a list of SimpleCategory objects,
    // each of which contain lists of their respective Plan objects. The database pulls down a list
    // of categories followed by their plans, so this function groups those into a list of SimpleCategory
    private fun getSimpleCategoriesFromCategoriesAndPlans(catsAndPlans: List<Any>): List<SimpleCategory> {
        val categories = mutableListOf<SimpleCategory>()
        var simpleCategory : SimpleCategory? = null
        var last = Any()
        for(item in catsAndPlans) {
            if(item is Category) {
                if(last is Plan && simpleCategory != null) {
                    categories.add(simpleCategory)
                }
                simpleCategory = SimpleCategory(item.name!!, mutableListOf<Plan>(), item.id)
            }
            if(item is Plan && simpleCategory != null) {
                simpleCategory.addPlan(item)
            }
            last = item
        }
        return categories
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bin.clear()
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
                recycler_view.adapter = context?.let { PlansAdapter(it, filteredResults) }
                recycler_view.adapter?.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.length == 0) {
                    recycler_view.adapter = context?.let { PlansAdapter(it, simpleCategories) }
                    recycler_view.adapter?.notifyDataSetChanged()
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
                recycler_view.adapter = context?.let { PlansAdapter(it, simpleCategories) }
                recycler_view.adapter?.notifyDataSetChanged()
                return true
            }
        }
        menu.findItem(R.id.action_search).setOnActionExpandListener(expandListener)
    }

    private fun searchDataByKeyword(keyword: String): List<SimpleCategory> {
        val categories = mutableListOf<SimpleCategory>()
        var simpleCategory : SimpleCategory? = null
        var last = Any()
        for(item in data) {
            if(item is Category) {
                if(last is Plan && simpleCategory != null) {
                    categories.add(simpleCategory)
                }
                simpleCategory = SimpleCategory(item.name!!, mutableListOf<Plan>(), item.id)
            }
            if(item is Plan && simpleCategory != null) {
                if(item.name?.contains(keyword) ?: false || item.description?.contains(keyword ) ?: false)
                simpleCategory.addPlan(item)
            }
            last = item
        }
        // check if the category is empty and remove it
        for(i in (categories.size - 1) downTo 0) {
            if(categories.get(i).plans.isEmpty()) {
                categories.removeAt(i)
            }
        }
        return categories
    }

    internal class CategoryViewHolder(val categoryView: View) : GroupViewHolder(categoryView) {

        fun setCategoryName(category: ExpandableGroup<*>) {
            if (category is SimpleCategory) {
                categoryView.textview_proposal_category_item_name.setText(category.name)
            }
        }

        override fun expand() {
            animateExpand()
        }

        override fun collapse() {
            animateCollapse()
        }

        private fun animateExpand() {
            val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 300
            rotate.fillAfter = true
            categoryView.imageview_proposal_category_item_arrow.setAnimation(rotate)
        }

        private fun animateCollapse() {
            val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 300
            rotate.fillAfter = true
            categoryView.imageview_proposal_category_item_arrow.setAnimation(rotate)
        }
    }

    internal class PlanViewHolder(val planView: View) : ChildViewHolder(planView) {
        fun setTextViewName(name: String?) {
            planView.textview_proposal_item_name.setText(name)
        }

        fun setTextViewDesc(desc: String?) {
            planView.textview_proposal_item_desc.setText(desc)
        }
    }
    internal inner class PlansAdapter(val context: Context, val data:List<SimpleCategory>) :
        ExpandableRecyclerViewAdapter<CategoryViewHolder, PlanViewHolder>(data) {

        override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): CategoryViewHolder {
            var view = LayoutInflater.from(context).inflate(R.layout.item_plan_category, parent, false)
            return CategoryViewHolder(view)
        }

        override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): PlanViewHolder {
            var view = LayoutInflater.from(context).inflate(R.layout.item_plan, parent, false)
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
            holder?.setTextViewDesc(proposal.description)
            holder?.planView?.textview_proposal_item_name?.setOnClickListener {
                val args = Bundle()
                args.putString(CategoryDetailsFragment.EXTRA_PLAN_ID, proposal.id)

                (context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        CategoryDetailsFragment.newInstance(args),
                        CategoryDetailsFragment.TAG
                    )
                    .addToBackStack(CategoryDetailsFragment.TAG)
                    .commit()
            }
            // favorites pre-checking and interaction
            holder?.planView?.checkbox_proposal_item_favorite?.isChecked = favorites.contains(proposal.id)

            holder?.planView?.checkbox_proposal_item_favorite?.setOnClickListener {
                if(holder?.planView?.checkbox_proposal_item_favorite?.isChecked ?: false) {
                    IOHelper.addFavoriteToSharedPrefs(context, proposal.id)
                } else {
                    IOHelper.removeFavoriteFromSharedPrefs(context, proposal.id)
                }
                favorites = IOHelper.loadFavoritesFromSharedPrefs(context)
            }
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

    }

    companion object {
        fun newInstance(): PlansFragment {
            return PlansFragment()
        }
    }

//    internal class PlansAdapter(val context: Context, val data: List<Any>) :
//        RecyclerView.Adapter<PlansAdapter.BaseViewHolder>() {
//        lateinit var recyclerView: RecyclerView
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//
//
//            when (viewType) {
//                R.layout.title_view_holder -> return CategoryViewHolder(
//                    LayoutInflater.from(parent.context).inflate(
//                        R.layout.title_view_holder,
//                        parent,
//                        false
//                    )
//                )
//                R.layout.item_view_holder -> return PlanViewHolder(
//                    LayoutInflater.from(parent.context).inflate(
//                        R.layout.item_view_holder,
//                        parent,
//                        false
//                    )
//                )
//            }
//
//            throw RuntimeException("Invalid viewholder type")
//        }
//
//        override fun getItemCount(): Int {
//            return data.size
//        }
//
//        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
//            when (holder) {
//                is CategoryViewHolder -> {
//                    holder.itemView.category_title.text = (data[position] as Category).name
//                }
//                is PlanViewHolder -> {
//                    holder.itemView.plan_text.text = (data[position] as Plan).name
//                }
//            }
//        }
//
//        override fun getItemViewType(position: Int): Int {
//            when (data[position]) {
//                is Category -> return R.layout.title_view_holder
//                is Plan -> return R.layout.item_view_holder
//            }
//
//            throw RuntimeException("Unsupported View Type")
//        }
//
//        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
//            super.onAttachedToRecyclerView(recyclerView)
//            this.recyclerView = recyclerView
//        }
//
//        open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//        inner class CategoryViewHolder(itemView: View) : BaseViewHolder(itemView) {
//            init {
//                itemView.setOnClickListener {
//                    val args = Bundle()
//                    val itemPosition = recyclerView.findContainingViewHolder(itemView)?.adapterPosition
//                    if (itemPosition == RecyclerView.NO_POSITION) {
//                        Log.e(TAG, "Invalid position clicked, try again?")
//                        return@setOnClickListener
//                    }
//                    val id = (data[itemPosition!!] as Category).id
//                    args.putString(CategoryDetailsFragment.EXTRA_CATEGORY_ID, id)
//
//                    (context as FragmentActivity).supportFragmentManager.beginTransaction()
//                        .replace(
//                            R.id.fragment_container,
//                            CategoryDetailsFragment.newInstance(args),
//                            CategoryDetailsFragment.TAG
//                        )
//                        .addToBackStack(CategoryDetailsFragment.TAG).commit()
//                }
//            }
//        }
//
//        inner class PlanViewHolder(itemView: View) : BaseViewHolder(itemView) {
//            init {
//                itemView.more_image.setColorFilter(itemView.resources.getColor(R.color.secondaryColor))
//                itemView.setOnClickListener {
//                    val args = Bundle()
//                    val itemPosition = recyclerView.findContainingViewHolder(itemView)?.adapterPosition
//                    if (itemPosition == RecyclerView.NO_POSITION) {
//                        Log.e(TAG, "Invalid position clicked, try again?")
//                        return@setOnClickListener
//                    }
//                    val id = (data[itemPosition!!] as Plan).id
//                    args.putString(CategoryDetailsFragment.EXTRA_PLAN_ID, id)
//
//                    (context as FragmentActivity).supportFragmentManager.beginTransaction()
//                        .replace(
//                            R.id.fragment_container,
//                            CategoryDetailsFragment.newInstance(args),
//                            CategoryDetailsFragment.TAG
//                        )
//                        .addToBackStack(CategoryDetailsFragment.TAG).commit()
//                }
//            }
//
//        }
//
//    }


}