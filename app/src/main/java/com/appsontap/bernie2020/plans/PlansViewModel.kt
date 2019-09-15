package com.appsontap.bernie2020.plans

import android.util.Log
import androidx.lifecycle.ViewModel
import com.appsontap.bernie2020.App
import com.appsontap.bernie2020.plans.model.PlansRepo
import com.appsontap.bernie2020.plans.model.UiState
import com.appsontap.bernie2020.util.TAG
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
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
            .fetchData()
            .zipWith(plansRepo.fetchExpandedItems(), (object : BiFunction<MutableList<Any>, Boolean, UiState.ListReady>{
                override fun apply(list: MutableList<Any>, t2: Boolean): UiState.ListReady {
                        return UiState.ListReady(list, )
            })
            ?.let {
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


