package com.appsontap.bernie2020.plans

import android.util.Log
import androidx.lifecycle.ViewModel
import com.appsontap.bernie2020.App
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.util.into
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 
 */
class PlansViewModel : ViewModel() {
    private val plansRepo = PlansRepo(App.get())
    private val bin = CompositeDisposable()
    private val dataEmitter = BehaviorSubject.create<List<Any>>()

    fun fetchData() {
        plansRepo
            .fetchData()?.let {
                it.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                        onSuccess = { list ->
                            Log.d(TAG, "Got the plan list data")
                            dataEmitter.onNext(list)
                        },
                        onError = { error ->
                            Log.e(TAG, "Couldn't get plan list data ${error.message}", error)
                        }
                    ).into(bin)
            }

    }
    
    fun dataReady() : Observable<List<Any>> {
        return dataEmitter.hide()
    }

    override fun onCleared() {
        super.onCleared()
        bin.clear()
    }
}


