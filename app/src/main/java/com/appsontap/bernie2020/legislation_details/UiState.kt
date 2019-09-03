package com.appsontap.bernie2020.legislation_details


sealed class UiState {
    class MarkUpReady(val attributedText : AttributedText) : UiState()
}