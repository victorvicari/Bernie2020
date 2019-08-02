package com.appsontap.bernie2020.legislation

import com.appsontap.bernie2020.AppDatabase
import com.appsontap.bernie2020.models.Legislation
import io.reactivex.Single

/**
 * Copyright (c) 2019 Pandora Media, Inc.
 */
class LegislationRepo{
    fun getLegislation() : Single<List<Legislation>>{
        return AppDatabase
            .getDatabase()
            .legislationDao()
            .getAll()
    }
}