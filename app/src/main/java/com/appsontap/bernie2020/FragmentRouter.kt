package com.appsontap.bernie2020

import androidx.fragment.app.Fragment


interface FragmentRouter{
    fun replaceFragment(fragment :Fragment)
    fun replaceWebViewFragmentWithTitle(url: String, title: String)
    fun setItemMenuSelected(id : Int)
}