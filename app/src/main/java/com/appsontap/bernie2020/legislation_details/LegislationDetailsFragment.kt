package com.appsontap.bernie2020.legislation_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.models.Legislation
import com.appsontap.bernie2020.plan_details.CategoryDetailsFragment
import com.appsontap.bernie2020.web.WebFragment
import com.google.gson.JsonArray
import com.google.gson.JsonNull
import kotlinx.android.synthetic.main.fragment_legislation_details.*
import java.lang.RuntimeException

/**
 * Feel the Bern
 */
class LegislationDetailsFragment : Fragment() {

    lateinit var legislation : Legislation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         arguments?.let {  
            legislation = it.getParcelable(EXTRA_KEY_LEGISLATION)!!
         }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_legislation_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        val adapter = LegislationPagerAdapter(requireActivity().supportFragmentManager)
        pager.adapter = adapter
        tab_layout.setupWithViewPager(pager)
    }

    inner class LegislationPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            when(position){
                2 -> return WebFragment.newInstance(Bundle().apply { 
                    this.putString(WebFragment.EXTRA_URL, legislation.url)
                })
            }
            return CategoryDetailsFragment.newInstance(Bundle())
        }

        override fun getCount(): Int {
            if(legislation.markup is JsonNull) return 0
            var count = 0
            legislation.url.let { 
                count++
            }
            count += (legislation.markup as JsonArray).size()
            return count
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position){
                0 -> getString(R.string.summary)
                1 -> getString(R.string.details)
                2 -> getString(R.string.entire_bill)
                else -> throw RuntimeException("Unhandled page title for Legislation $legislation")

            }
        }
    }
 
    companion object {
        const val EXTRA_KEY_LEGISLATION = "EXTRA_KEY_LEGISLATION"
        fun newInstance(args: Bundle): LegislationDetailsFragment {
            val fragment = LegislationDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}