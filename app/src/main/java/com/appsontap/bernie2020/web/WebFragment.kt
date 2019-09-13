package com.appsontap.bernie2020.web

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.R
import kotlinx.android.synthetic.main.fragment_web.*

class WebFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(EXTRA_TITLE)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.options_menu_webview, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_copy_webview_url -> {
                val activity = requireActivity()
                val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", webview.url)
                clipboard.primaryClip = clip
                Toast.makeText(requireContext(), "Link Copied!", Toast.LENGTH_SHORT).show();
                return true
            }
            R.id.action_open_in_browser -> {

                return true
            }
            else -> return false
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