package com.appsontap.bernie2020.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import com.appsontap.bernie2020.App
import com.appsontap.bernie2020.TAG
import com.appsontap.bernie2020.into
import com.appsontap.bernie2020.plans.PlansRepo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class FavoritesViewModel : ViewModel() {
    val favoritesRepo = FavoritesRepo(App.get())
    val bin = CompositeDisposable()
    val dataEmitter = BehaviorSubject.create<List<Any>>()

    fun fetchPlanData() {
        favoritesRepo
            .fetchPlanData()?.let {
                it.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                        onSuccess = { list ->
                            Log.d(TAG, "onSuccess?!")
                            dataEmitter.onNext(list)
                        },
                        onError = {
                            Log.e(TAG, "Couldn't get favorites list data ${it.message}", it)
                        }
                    ).into(bin)
            }
    }

    fun fetchLegislationData() {
        favoritesRepo
            .fetchLegislationData()?.let {
                it.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                        onSuccess = { list ->
                            Log.d(TAG, "onSuccess?!")
                            dataEmitter.onNext(list)
                        },
                        onError = {
                            Log.e(TAG, "Couldn't get favorites list data ${it.message}", it)
                        }
                    ).into(bin)
            }
    }

    override fun onCleared() {
        super.onCleared()
        bin.clear()
    }
}