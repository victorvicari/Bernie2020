package com.appsontap.bernie2020.plans

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.appsontap.bernie2020.App
import com.appsontap.bernie2020.TAG
import com.appsontap.bernie2020.into
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 
 */
class PlansViewModel : ViewModel() {
    val plansRepo = PlansRepo(App.get())
    val bin = CompositeDisposable()
    val dataEmitter = BehaviorSubject.create<List<Any>>()

    fun fetchData() {
        plansRepo
            .fetchData()?.let {
                it.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                        onSuccess = { list ->
                            Log.d(TAG, "onSuccess?!")
                            dataEmitter.onNext(list)
                        },
                        onError = {
                            Log.e(TAG, "Couldn't get plan list data ${it.message}", it)
                        }
                    ).into(bin)
            }

    }

    override fun onCleared() {
        super.onCleared()
        bin.clear()
    }
}


