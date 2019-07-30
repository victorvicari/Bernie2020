package com.appsontap.bernie2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.appsontap.bernie2020.plans.PlansFragment
import com.appsontap.bernie2020.timeline.TimelineFragment
import com.appsontap.bernie2020.web.WebFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, 0, 0)

        drawer.addDrawerListener(toggle)
        nav_view.setNavigationItemSelectedListener(this)
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
            // TODO add to bottom nav: getString(R.string.events_map) -> url = getString(R.string.events_url)
            // TODO add to bottom nav: getString(R.string.canvas) -> url = getString(R.string.bern_url)
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

        if (url != null) {
            val args = Bundle()
            args.putString(WebFragment.EXTRA_URL, url)
            args.putString(WebFragment.EXTRA_TITLE, toolbarTitle)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WebFragment.newInstance(args), WebFragment.TAG).commit()
        } else {
            lateinit var fragment : Fragment
            when(item.title){
                getString(R.string.plans) -> fragment = PlansFragment.newInstance()
                getString(R.string.timeline) -> fragment = TimelineFragment.newInstance()
            }
            
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.TAG).addToBackStack(fragment.TAG).commit()
        }


        drawer.closeDrawer(GravityCompat.START)
        return true
    }

//    override fun getSystemService(name: String): Any? {
//        if(name == )
//        return super.getSystemService(name)
//    }
}
