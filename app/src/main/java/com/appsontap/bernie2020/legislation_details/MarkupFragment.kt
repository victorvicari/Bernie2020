package com.appsontap.bernie2020.legislation_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.appsontap.bernie2020.BaseFragment
import com.appsontap.bernie2020.R
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.util.into
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_markup.*


class MarkupFragment : BaseFragment() {

    private val viewModel: MarkupViewModel by lazy {
        ViewModelProviders.of(this).get(MarkupViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_markup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(EXTRA_MARKUP)?.let {
            viewModel.viewLoaded(it)
        }
        viewModel.uiReady()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { uiState ->
                when (uiState) {
                    is UiState.MarkUpReady -> textview.text =
                        (uiState.attributedText as SpannableAttributedTextImpl).builder
                }
            },
                onError = {
                    Log.e(TAG, "Couldn't build legislation detail text ${it.message}", it)
                })
            .into(bin)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bin.clear()
    }

    companion object {
        const val EXTRA_MARKUP = "EXTRA_MARKUP"

        fun newInstance(args: Bundle): MarkupFragment {
            return MarkupFragment().apply {
                this.arguments = args
            }
        }
    }
}