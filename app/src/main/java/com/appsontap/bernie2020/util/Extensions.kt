package com.appsontap.bernie2020.util

import androidx.fragment.app.FragmentManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

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
