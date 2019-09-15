package com.appsontap.bernie2020.plans.model

sealed class UiState() {
    class ListReady(items : List<Any>, expandedItems: BooleanArray)
}