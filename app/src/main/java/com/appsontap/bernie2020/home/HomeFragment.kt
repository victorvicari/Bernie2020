package com.appsontap.bernie2020.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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


        val ssTop = SpannableString(getResources().getString(R.string.volunteer_btext1))
        ssTop.setSpan(
            TextAppearanceSpan(activity?.applicationContext, R.style.VolunteerTopTextStyle),
            0,
            getResources().getString(R.string.volunteer_btext1).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val ssBottom = SpannableString(getResources().getString(R.string.volunteer_btext2))

        ssBottom.setSpan(
            TextAppearanceSpan(
                activity?.applicationContext,
                R.style.VolunteerTopTextStyle2
            ),
            0,
            getResources().getString(R.string.volunteer_btext2).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val volButton = activity?.findViewById(R.id.volunteerButton) as Button
       volButton.text = TextUtils.concat(ssTop, "\n", ssBottom)

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


        ny_registration_button.setOnClickListener {
            (requireActivity() as FragmentRouter).run{
                replaceWebViewFragmentWithTitle(getString(R.string.ny_resgister_url), getString(R.string.web_ny_resgister))
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