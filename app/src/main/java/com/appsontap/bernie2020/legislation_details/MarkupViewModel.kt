package com.appsontap.bernie2020.legislation_details

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


class MarkupViewModel : ViewModel() {
    private val emitter = BehaviorSubject.create<UiState>()
    private val parser = MarkupParser()
    
    fun viewLoaded(markupString: String) {
        parser.parse(markupString)
        val text = SpannableAttributedTextImpl()
        text.builder = parser.parse(markupString)
        
        emitter.onNext(UiState.MarkUpReady(text))
    }

    fun uiReady(): Observable<UiState> {
        return emitter.hide()
    }
}