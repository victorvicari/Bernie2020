package com.appsontap.bernie2020.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.loadWebFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(){
    private val viewModel : HomeFragmentViewModel by lazy { 
        ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        volunteer_button.setOnClickListener {
            (requireActivity() as AppCompatActivity).loadWebFragment(getString(R.string.volunteer_url), getString(R.string.web_title_volunteer))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    
    companion object{
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}