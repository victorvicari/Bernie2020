package com.appsontap.bernie2020

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
