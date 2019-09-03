package com.appsontap.bernie2020.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.R
import kotlinx.android.synthetic.main.fragment_web.*

class WebFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(EXTRA_TITLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onStart() {
        super.onStart()
        webview.settings.javaScriptEnabled = true
        webview.loadUrl(arguments?.getString(EXTRA_URL))
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progress_bar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress_bar.visibility = View.GONE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        webview.webViewClient = null
    }

    // returns false if there's no history to go back to
     fun onBackPressed() : Boolean {
        return if(webview.canGoBack()) {
            webview.goBack()
            true
        } else {
            false
        }
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_URL = "url"
        fun newInstance(args: Bundle): WebFragment {
            val fragment = WebFragment()
            fragment.arguments = args
            return fragment
        }
    }
}