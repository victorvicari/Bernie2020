package com.appsontap.bernie2020

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Feel the Bern
 */
open class BaseFragment : Fragment(){
    var title : String? = null
    val bin = CompositeDisposable()
    override fun onStart() {
        super.onStart()
        title?.let { 
            //https://stackoverflow.com/questions/15576224/how-to-display-android-actionbar-title-without-truncation-occurring
            (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        }
    }
}