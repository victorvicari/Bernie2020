package com.appsontap.bernie2020.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import com.appsontap.bernie2020.App
import com.appsontap.bernie2020.plan_details.UiState
import com.appsontap.bernie2020.util.TAG
import com.appsontap.bernie2020.util.into
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class FavoritesViewModel : ViewModel() {
    private val favoritesRepo = FavoritesRepo(App.get())
    private val bin = CompositeDisposable()
    val dataEmitter = BehaviorSubject.create<UiState.ListReady>()

    fun fetchData() {
        favoritesRepo
            .fetchData().let {
                it.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                        onSuccess = { list ->
                            Log.d(TAG, "onSuccess")
                            dataEmitter.onNext(UiState.ListReady(list, setOf()))
                        },
                        onError = { error ->
                            Log.e(TAG, "Couldn't get favorites list data ${error.message}", error)
                        }
                    ).into(bin)
            }
    }


    override fun onCleared() {
        super.onCleared()
        bin.clear()
    }
}