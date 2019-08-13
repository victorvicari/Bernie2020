package com.appsontap.bernie2020.legislation_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.R

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class MarkupFragment : BaseFragment(){
    
    lateinit var markup : String
    val viewModel : MarkupViewModel by lazy { 
         ViewModelProviders.of(this).get(MarkupViewModel::class.java)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        markup = arguments?.getString(EXTRA_MARKUP)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_markup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel
    }
    
    companion object{
        const val EXTRA_MARKUP = "EXTRA_MARKUP"
        
        fun newInstance(args: Bundle): MarkupFragment {
            return MarkupFragment().apply { 
                this.arguments = args
            }
        }
    }
}