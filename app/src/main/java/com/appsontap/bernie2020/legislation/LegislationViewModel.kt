package com.appsontap.bernie2020.legislation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.appsontap.bernie2020.plan_details.UiState
import com.appsontap.bernie2020.util.TAG
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject


class LegislationViewModel : ViewModel() {
    private val repo = LegislationRepo()
    val dataEmitter = BehaviorSubject.create<UiState>()
    fun fetchData() {
        repo.getLegislation()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribeBy(onSuccess = {
                Log.d(TAG, "Got legislation for LegislationViewModel")
                dataEmitter.onNext(UiState.ListReady(it, setOf()))
            },
                onError = {
                    Log.e(TAG, "Couldn't get legislation ${it.message}", it)
                })
    }
}