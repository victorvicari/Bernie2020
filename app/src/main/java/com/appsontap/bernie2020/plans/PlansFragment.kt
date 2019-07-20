package com.appsontap.bernie2020.plans

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.*
import com.appsontap.bernie2020.models.Category
import com.appsontap.bernie2020.models.Plan
import com.appsontap.bernie2020.plan_details.CategoryDetailsFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.title_view_holder.view.*
import kotlinx.android.synthetic.main.fragment_plans.*
import kotlinx.android.synthetic.main.item_view_holder.view.*
import java.lang.RuntimeException


//todo need to diff the list here so when back is pressed from a detail fragment the list doesn't go back to the top
class PlansFragment : Fragment() {

    private val viewModel: PlansViewModel by lazy {
        ViewModelProviders.of(this).get(PlansViewModel::class.java)
    }
    private val bin = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.fetchData()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel
            .dataEmitter
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

    internal class PlansAdapter(val context: Context, val data: List<Any>) :
        RecyclerView.Adapter<PlansAdapter.BaseViewHolder>() {
        lateinit var recyclerView: RecyclerView

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            when (viewType) {
                R.layout.title_view_holder -> return CategoryViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.title_view_holder,
                        parent,
                        false
                    )
                )
                R.layout.item_view_holder -> return PlanViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_view_holder,
                        parent,
                        false
                    )
                )
            }

            throw RuntimeException("Invalid viewholder type")
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            when (holder) {
                is CategoryViewHolder -> {
                    holder.itemView.category_title.text = (data[position] as Category).name
                }
                is PlanViewHolder -> {
                    holder.itemView.plan_text.text = (data[position] as Plan).name
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            when (data[position]) {
                is Category -> return R.layout.title_view_holder
                is Plan -> return R.layout.item_view_holder
            }

            throw RuntimeException("Unsupported View Type")
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            this.recyclerView = recyclerView
        }

        open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        inner class CategoryViewHolder(itemView: View) : BaseViewHolder(itemView) {
            init {
                itemView.setOnClickListener {
                    val args = Bundle()
                    val itemPosition = recyclerView.findContainingViewHolder(itemView)?.adapterPosition
                    val id = (data.get(itemPosition!!) as Category).id
                    args.putString(CategoryDetailsFragment.EXTRA_CATEGORY_ID, id)

                    (context as FragmentActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CategoryDetailsFragment.newInstance(args), CategoryDetailsFragment.TAG)
                        .addToBackStack(CategoryDetailsFragment.TAG).commit()
                }
            }
        }

        inner class PlanViewHolder(itemView: View) : BaseViewHolder(itemView) {
            init {
                itemView.more_image.setColorFilter(itemView.resources.getColor(R.color.secondaryColor))
            }

        }

    }

    companion object {
        fun newInstance(): PlansFragment {
            return PlansFragment()
        }
    }
}