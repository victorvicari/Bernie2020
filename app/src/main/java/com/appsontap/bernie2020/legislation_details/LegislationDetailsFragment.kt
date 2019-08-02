package com.appsontap.bernie2020.legislation_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.plan_details.CategoryDetailsFragment
import com.appsontap.bernie2020.web.WebFragment
import kotlinx.android.synthetic.main.fragment_legislation_details.*

/**
 * Feel the Bern
 */
class LegislationDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//            val args = Bundle()
//            args.putString(WebFragment.EXTRA_URL, resources.getString(R.string.debt_calc_url))
//            args.putString(WebFragment.EXTRA_TITLE, "test")
//            return WebFragment.newInstance(args)
            return CategoryDetailsFragment.newInstance(Bundle())
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "a title"
        }
    }

    companion object {
        fun newInstance(): LegislationDetailsFragment {
            return LegislationDetailsFragment()
        }
    }
}