package com.appsontap.bernie2020.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.MainActivity
import com.appsontap.bernie2020.R
import kotlinx.android.synthetic.main.fragment_web.*

class WebFragment : Fragment(){
    val viewModel: WebViewModel by lazy {
        ViewModelProviders.of(this).get(WebViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onStart() {
        super.onStart()
        webview.settings.javaScriptEnabled = true
        webview.loadUrl(arguments?.getString(EXTRA_URL))
        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString(EXTRA_TITLE)
    }

    override fun onStop() {
        super.onStop()
    }

    companion object{
        val EXTRA_TITLE = "title"
        val EXTRA_URL = "url"
        fun newInstance(args: Bundle) : WebFragment {
            val fragment = WebFragment()
            fragment.arguments = args
            return fragment
        }
    }
}