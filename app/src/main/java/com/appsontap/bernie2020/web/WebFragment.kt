package com.appsontap.bernie2020.web

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.util.TAG
import kotlinx.android.synthetic.main.fragment_web.*

class WebFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(EXTRA_TITLE)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_web, container, false)
        if(savedInstanceState != null) {
            // this isn't being called. Don't know why. onSaveInstanceState *IS* being called.
            Log.d(TAG, "state allegedly restored")
            webview.restoreState(savedInstanceState.getBundle(KEY_WEBVIEW_STATE))
        }
        return rootView
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onStart() {
        super.onStart()
        if(webview.copyBackForwardList().size == 0) {
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
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.options_menu_webview, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_copy_webview_url -> {
                val activity = requireActivity()
                val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(CLIPBOARD_LABEL, webview.url)
                clipboard.primaryClip = clip
                Toast.makeText(requireContext(), getString(R.string.webview_link_copied), Toast.LENGTH_SHORT).show();
                return true
            }
            R.id.action_open_in_browser -> {
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.setData(Uri.parse(webview.url))
                startActivity(browserIntent)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = Bundle()
        webview.saveState(bundle)
        outState.putBundle(KEY_WEBVIEW_STATE, bundle)
        Log.d(TAG, "instance state saved!")
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_URL = "url"
        const val CLIPBOARD_LABEL = "webview url"
        const val KEY_WEBVIEW_STATE = "webview state"
        fun newInstance(args: Bundle): WebFragment {
            val fragment = WebFragment()
            fragment.arguments = args
            return fragment
        }
    }
}