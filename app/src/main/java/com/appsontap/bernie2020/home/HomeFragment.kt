package com.appsontap.bernie2020.home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.FragmentRouter
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.timeline.TimelineFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.app_name)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        events_button.setOnClickListener {
            (requireActivity() as FragmentRouter).run {
                setItemMenuSelected(R.id.bot_nav_events_map)
            }
        }

        val ss2 = SpannableString(getResources().getString(R.string.volunteer_btext))
        ss2.setSpan(RelativeSizeSpan(.8f), 23, 54, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss2.setSpan(StyleSpan(Typeface.ITALIC), 0, 22,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val tv = activity?.findViewById(R.id.volunteerButton) as Button
        tv.text = ss2


       volunteerButton.setOnClickListener {
            (requireActivity() as FragmentRouter).replaceWebViewFragmentWithTitle(
                getString(R.string.volunteer_url),
                getString(R.string.web_title_volunteer)
            )
        }


        timeline_button.setOnClickListener {
            (requireActivity() as FragmentRouter).run {
               replaceFragment(TimelineFragment.newInstance())
                setItemMenuSelected(R.id.bot_nav_more)
            }
        }

        plans_button.setOnClickListener {
            (requireActivity() as FragmentRouter).run {
                setItemMenuSelected(R.id.bot_nav_plans)
            }
        }
        news_button.setOnClickListener {
            (requireActivity() as FragmentRouter).run {
                replaceWebViewFragmentWithTitle(getString(R.string.news_url),getString(R.string.web_title_news))
                setItemMenuSelected(R.id.bot_nav_more)
            }
        }

        donateButton.setOnClickListener {
            (requireActivity() as FragmentRouter).run{
                replaceWebViewFragmentWithTitle(getString(R.string.donate_url), getString(R.string.web_title_donate))
                setItemMenuSelected(R.id.bot_nav_more)
            }
        }


    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as FragmentRouter).setItemMenuSelected(R.id.bot_nav_home)

    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}