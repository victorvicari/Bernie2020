package com.appsontap.bernie2020

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.appsontap.bernie2020.Constants.Companion.BACK_STACK_ROOT_TAG
import com.appsontap.bernie2020.web.WebFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 
 */
    fun Disposable.into(compositeDisposable: CompositeDisposable?) {
        compositeDisposable?.let {
            if (it.isDisposed) {
                throw IllegalStateException(
                    "attempted to add Disposable into CompositeDisposable that is already disposed"
                )
            } else {
                it.add(this)
            }
        }
    }

     fun AppCompatActivity.loadWebFragment(url: String?, title: String?) {
        if (url != null && title != null) {
            val args = Bundle()
            args.putString(WebFragment.EXTRA_URL, url)
            args.putString(WebFragment.EXTRA_TITLE, title)
            supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WebFragment.newInstance(args), WebFragment.TAG)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit()
        }
    }
