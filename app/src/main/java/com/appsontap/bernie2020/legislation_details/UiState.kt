package com.appsontap.bernie2020.legislation_details

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
sealed class UiState {
    class MarkUpReady(val attributedText : AttributedText) : UiState()
}