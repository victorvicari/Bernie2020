package com.appsontap.bernie2020.timeline

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.getSystem
import android.graphics.Canvas
import android.graphics.Rect
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.models.TimelineItem


/**
 * Feel the Bern
 */
class RecyclerSectionItemDecoration(
    private val mContext: Context,
    private val sticky: Boolean,
    private val timeline: Timeline
) :
    RecyclerView.ItemDecoration() {


    private var headerView: View? = null
    private var header: TextView? = null

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        var draw_header = false
        if (headerView == null) {
            headerView = inflateHeaderView(parent)
            header = headerView!!.findViewById(R.id.tv_header) as TextView
            fixLayoutSize(headerView!!, parent)
        }


        var previousHeader: CharSequence = ""
        var title: String = ""

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (position > 0) {
                if (timeline.getTypeForPosition(position - 1) == ViewType.YEAR) {
                    title = timeline.getItemAtPosition(position - 1).toString()
                } else {
                    val timelineItem = timeline.getItemAtPosition(
                        position - 1
                    ) as TimelineItem
                    title = timelineItem.year
                }
            } else {
                title = mContext.getString(R.string.first_timeline_year)


            }
            header?.text = title
            if (previousHeader != title) {
                drawHeader(c, child, headerView!!)
                previousHeader = title
            }
        }


    }

    private fun drawHeader(c: Canvas, child: View, headerView: View) {

        c.save()
        if (sticky) {
            c.translate(0f, Math.max(0, child.top - headerView.height).toFloat())
        } else {
            c.translate(0f, (child.top - headerView.height).toFloat())
        }
        headerView.draw(c)
        c.restore()

    }

    private fun inflateHeaderView(parent: RecyclerView): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.view_header, parent, false)
    }

    //For setting the size of the header/year blocks. Essentially you draw one R.id.tv_header for every date not just the header.
    // Each one only occupies the space of a year section in the recyclerview, not the entire screen.
    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.UNSPECIFIED
        )

        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }


}
