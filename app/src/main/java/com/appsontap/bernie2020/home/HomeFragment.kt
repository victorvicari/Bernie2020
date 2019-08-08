package com.appsontap.bernie2020.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.FragmentRouter
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.timeline.TimelineFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {
    private val viewModel: HomeFragmentViewModel by lazy {
        ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)
    }

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
        volunteer_button.setOnClickListener {
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
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as FragmentRouter).setItemMenuSelected(R.id.bot_nav_home)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}