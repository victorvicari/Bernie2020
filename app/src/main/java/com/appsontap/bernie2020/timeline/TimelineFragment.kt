package com.appsontap.bernie2020.timeline

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.util.into
import com.appsontap.bernie2020.models.TimelineItem
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_timeline.*
import kotlinx.android.synthetic.main.timeline_image_viewholder.view.*
import kotlinx.android.synthetic.main.timeline_text_viewholder.view.*
import kotlinx.android.synthetic.main.timeline_year_viewholder.view.*
import java.lang.RuntimeException
import android.content.Intent
import android.net.Uri
import com.appsontap.bernie2020.BaseFragment


/**
 * Feel the Bern
 */
class TimelineFragment : BaseFragment() {

    private val viewModel: TimelineViewModel by lazy {
        ViewModelProviders.of(this).get(TimelineViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.drawer_timeline)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.timelineReady()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    recycler_view.adapter = TimelineAdapter(it)
                },
                onError = {
                    Log.e(TAG, "Couldn't display timeline ${it.message}", it)
                }).into(bin)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bin.clear()
    }

    inner class TimelineAdapter(val timeline: Timeline) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                R.layout.timeline_year_viewholder ->
                    YearViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.timeline_year_viewholder,
                            parent,
                            false
                        )
                    )
                R.layout.timeline_image_viewholder ->
                    ImageDescriptionViewHolder2(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.timeline_image_viewholder,
                            parent,
                            false
                        )
                    )
                R.layout.timeline_text_viewholder ->
                    TextViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.timeline_text_viewholder,
                            parent,
                            false
                        )
                    )
                else -> throw RuntimeException("Cant create this type of viewholder")
            }
        }

        override fun getItemCount(): Int {
            return timeline.totalItemCount()
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is YearViewHolder -> holder.bind(timeline.getItemAtPosition(position) as String)
                is ImageDescriptionViewHolder2 -> {
                    val item = timeline.getItemAtPosition(position) as TimelineItem
                    holder.bind(item.image_url!!, item.description, item.image_resource)
                }
                is TextViewHolder -> {
                    val item = timeline.getItemAtPosition(position) as TimelineItem
                    holder.bind(item.description)
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (timeline.getTypeForPosition(position)) {
                ViewType.YEAR -> R.layout.timeline_year_viewholder
                ViewType.IMAGE -> R.layout.timeline_image_viewholder
                ViewType.TEXT -> R.layout.timeline_text_viewholder
            }
        }

        inner class YearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(year: String) {
                itemView.year_text_view.text = year
            }
        }

        inner class ImageDescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(url: String, description: String) {
                Glide.with(itemView).load(url).into(itemView.image_view)
                itemView.image_description_text_view.text = description
                itemView.image_view.setOnClickListener {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
            }
        }

        inner class ImageDescriptionViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(url: String, description: String, img_resource: String?) {
                if (img_resource!=null){
                    val id = resources.getIdentifier(img_resource, "drawable", activity!!.packageName)
                    itemView.image_view.setImageResource(id)
                    //itemView.image_view.setBackground(getResources().getDrawable(id))
                }
                itemView.image_description_text_view.text = description
                itemView.image_view.setOnClickListener {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
            }
        }

        inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(description: String) {
                itemView.item_text_view.text = description
            }
        }

    }

    companion object {
        fun newInstance(): TimelineFragment {
            return TimelineFragment()
        }
    }
}