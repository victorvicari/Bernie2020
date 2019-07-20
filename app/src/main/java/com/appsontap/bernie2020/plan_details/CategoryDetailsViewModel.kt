package com.appsontap.bernie2020.plan_details

import androidx.lifecycle.ViewModel
import io.reactivex.Single

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class CategoryDetailsViewModel : ViewModel() {
    
    fun fetchData(categoryId: String): Single<UiState.ListReady> {
        return CategoryDetailRepo().fetchDataForCategory(categoryId)
    }
}