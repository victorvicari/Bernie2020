package com.appsontap.bernie2020.plan_details

import androidx.lifecycle.ViewModel
import io.reactivex.Single

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class CategoryDetailsViewModel : ViewModel() {
    
    fun categoryDetails(categoryId: String): Single<UiState.ListReady> {
        return CategoryDetailRepo().fetchDataForCategory(categoryId)
    }
    
    fun planDetails(planId: String): Single<UiState.ListReady> {
        return CategoryDetailRepo().fetchDataForPlan(planId)
    }
}