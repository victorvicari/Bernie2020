package com.appsontap.bernie2020.plan_details

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
sealed class UiState(){
     class ListReady(val items : List<Any>, val titleIndexes: Set<Int>) : UiState()
}