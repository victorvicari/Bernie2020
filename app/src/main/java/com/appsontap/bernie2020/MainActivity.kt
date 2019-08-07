package com.appsontap.bernie2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.appsontap.bernie2020.legislation.LegislationFragment
import androidx.fragment.app.FragmentManager
import com.appsontap.bernie2020.Constants.Companion.BACK_STACK_ROOT_TAG
import com.appsontap.bernie2020.home.HomeFragment
import com.appsontap.bernie2020.plans.PlansFragment
import com.appsontap.bernie2020.timeline.TimelineFragment
import com.appsontap.bernie2020.web.WebFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, 0, 0)

        drawer.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        setFragmentToggleBetweenUpAndHamburger()
        Log.d(TAG, "LOOK AT BACKSTACK COUNT: " + supportFragmentManager.backStackEntryCount.toString())

        nav_view_bottom.setOnNavigationItemSelectedListener(onBottomNavigationSelectedListener)
        
        if(supportFragmentManager.backStackEntryCount == 0){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, HomeFragment.newInstance(), HomeFragment.TAG)
                .commit()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "Menu item selected")
        var url: String? = null
        var toolbarTitle: String? = null

        when (item.title) {
            getString(R.string.drawer_news) -> {
                url = getString(R.string.news_url)
                toolbarTitle = getString(R.string.web_title_news)
            }
            getString(R.string.drawer_taxes) -> {
                url = getString(R.string.taxes_url)
                toolbarTitle = getString(R.string.web_title_taxes)
            }
            getString(R.string.drawer_press) -> {
                url = getString(R.string.press_url)
                toolbarTitle = getString(R.string.web_title_press)
            }
            getString(R.string.drawer_jobs) -> {
                url = getString(R.string.jobs_url)
                toolbarTitle = getString(R.string.web_title_jobs)
            }
            getString(R.string.drawer_podcast) -> {
                url = getString(R.string.podcast_url)
                toolbarTitle = getString(R.string.web_title_podcast)
            }
            getString(R.string.drawer_debt_calculator) -> {
                url = getString(R.string.debt_calc_url)
                toolbarTitle = getString(R.string.web_title_debt_calculator)
            }
            getString(R.string.drawer_how_to_vote) -> {
                url = getString(R.string.how_to_vote_url)
                toolbarTitle = getString(R.string.web_title_vote)
            }
        }

        nav_view_bottom.setSelectedItemId(R.id.bot_nav_more)


        // will dump all the fragments from the stack when switching to a new top-level fragment
        supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        if (url != null && toolbarTitle != null) {
            val args = Bundle()
            args.putString(WebFragment.EXTRA_URL, url)
            args.putString(WebFragment.EXTRA_TITLE, toolbarTitle)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WebFragment.newInstance(args), WebFragment.TAG)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit()
        } else {
            lateinit var fragment : Fragment
            when(item.title){
                getString(R.string.plans) -> fragment = PlansFragment.newInstance()
                getString(R.string.timeline) -> fragment = TimelineFragment.newInstance()
                getString(R.string.drawer_legislation) -> fragment = LegislationFragment.newInstance()
            }
            
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.TAG)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit()
        }


        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private val onBottomNavigationSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {

        when(it.itemId) {
            R.id.bot_nav_home -> {
                supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container, HomeFragment.newInstance(),
                        HomeFragment.TAG
                    )
                    .addToBackStack(BACK_STACK_ROOT_TAG)
                    .commit()
                return@OnNavigationItemSelectedListener true 
            }
            R.id.bot_nav_events_map -> {
                loadWebFragment(getString(R.string.events_url), getString(R.string.web_title_events))
                return@OnNavigationItemSelectedListener true
            }
            R.id.bot_nav_canvass -> {
                loadWebFragment(getString(R.string.bern_url), getString(R.string.web_title_canvass))
                return@OnNavigationItemSelectedListener true
            }
            R.id.bot_nav_more -> {
                toggleDrawer()
                return@OnNavigationItemSelectedListener true
            }
            R.id.bot_nav_plans -> {
                supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PlansFragment.newInstance(),
                        PlansFragment.TAG)
                    .addToBackStack(BACK_STACK_ROOT_TAG)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            // TODO add home fragment functionality
            else -> return@OnNavigationItemSelectedListener false
        }
    }

    private fun toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    private fun loadWebFragment(url: String?, title: String?) {
        if (url != null && title != null) {
            val args = Bundle()
            args.putString(WebFragment.EXTRA_URL, url)
            args.putString(WebFragment.EXTRA_TITLE, title)
            supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WebFragment.newInstance(args), WebFragment.TAG)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit()
        }
    }

    private fun setFragmentToggleBetweenUpAndHamburger() {
        supportFragmentManager.addOnBackStackChangedListener(object : FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {
                Log.d(TAG, "BACKSTACK CHANGED")

                if (supportFragmentManager.backStackEntryCount > 1) {
                    toggle.isDrawerIndicatorEnabled = false
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)// show back button
                    toolbar.setNavigationOnClickListener { onBackPressed() }
                    Log.d(TAG, "BACKSTACK > 0")
                } else {
                    //show hamburger
                    Log.d(TAG, "onBackStackChanged: HERE")
                    toggle.isDrawerIndicatorEnabled = true
                    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                    toggle.syncState()
                    toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
                }
            }
        })
    }

}
