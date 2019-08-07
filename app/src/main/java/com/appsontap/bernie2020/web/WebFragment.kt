package com.appsontap.bernie2020.web

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.R
import kotlinx.android.synthetic.main.fragment_web.*

class WebFragment : Fragment() {
    val viewModel: WebViewModel by lazy {
        ViewModelProviders.of(this).get(WebViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onStart() {
        super.onStart()
        webview.settings.javaScriptEnabled = true
        webview.loadUrl(arguments?.getString(EXTRA_URL))
        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString(EXTRA_TITLE)
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

    companion object {
        val EXTRA_TITLE = "title"
        val EXTRA_URL = "url"
        fun newInstance(args: Bundle): WebFragment {
            val fragment = WebFragment()
            fragment.arguments = args
            return fragment
        }
    }
}