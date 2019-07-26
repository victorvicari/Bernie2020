package com.appsontap.bernie2020.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_timeline.*

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class TimelineFragment : Fragment() {

    val viewModel: TimelineViewModel by lazy {
        ViewModelProviders.of(this).get(TimelineViewModel::class.java)
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

                })
    }

    inner class TimelineAdapter(val timeline: Timeline) : RecyclerView.Adapter<TimelineAdapter.BaseViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemCount(): Int {
            return timeline.totalItemCount()
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }

        inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }
    }

    companion object {
        fun newInstance(): TimelineFragment {
            return TimelineFragment()
        }
    }
}