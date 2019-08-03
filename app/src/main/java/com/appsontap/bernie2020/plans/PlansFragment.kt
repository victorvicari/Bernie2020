package com.appsontap.bernie2020.plans

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.models.SimpleCategory
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


//todo need to diff the list here so when back is pressed from a detail fragment the list doesn't go back to the top
class PlansFragment : Fragment() {

    private val viewModel: PlansViewModel by lazy {
        ViewModelProviders.of(this).get(PlansViewModel::class.java)
    }
    private val bin = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchData()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null) {
            viewModel
                .dataEmitter
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        val categories = getSimpleCategoriesFromCategoriesAndPlans(it)
                        // TODO rewrite adapters for the expandable recycler views
                        recycler_view.adapter = PlansAdapter(requireContext(), categories as List<SimpleCategory>)
                        Log.d("LOOK AT ME", categories.toString())

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
    private fun getSimpleCategoriesFromCategoriesAndPlans(catsAndPlans: List<Any>): Any {
        val categories = mutableListOf<SimpleCategory>()
        var simpleCategory : SimpleCategory? = null
        var last = Any()
        for(item in catsAndPlans) {
            if(item is Category) {
                if(last is Plan && simpleCategory != null) {
                    categories.add(simpleCategory)
                }
                simpleCategory = SimpleCategory(item.name!!, mutableListOf<Plan>())
            }
            if(item is Plan && simpleCategory != null) {
                simpleCategory.addPlan(item)
            }
            last = item
        }
        return categories
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        bin.clear()
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
    internal class PlansAdapter(val context: Context, val data:List<SimpleCategory>) :
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
        }

        override fun onBindGroupViewHolder(holder: CategoryViewHolder?, flatPosition: Int, group: ExpandableGroup<*>) {
            holder?.setCategoryName(group)
            if (isGroupExpanded(group)) {
                holder?.expand()
            }
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

    companion object {
        fun newInstance(): PlansFragment {
            return PlansFragment()
        }
    }
}