package com.appsontap.bernie2020.plan_details

/**
 * Feel the Bern
 */
sealed class UiState(){
     class ListReady(val items : List<Any>, val titleIndexes: Set<Int>) : UiState()
}