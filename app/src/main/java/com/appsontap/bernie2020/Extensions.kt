package com.appsontap.bernie2020

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
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

     fun FragmentManager.getTopFragmentEntry(): FragmentManager.BackStackEntry {
        return getBackStackEntryAt(backStackEntryCount - 1)
    }
