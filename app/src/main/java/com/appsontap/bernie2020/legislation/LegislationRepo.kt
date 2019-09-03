package com.appsontap.bernie2020.legislation

import com.appsontap.bernie2020.database.AppDatabase
import com.appsontap.bernie2020.models.Legislation
import io.reactivex.Single

/**
 * Feel the Bern
 */
class LegislationRepo{
    fun getLegislation() : Single<List<Legislation>>{
        return AppDatabase
            .getDatabase()
            .legislationDao()
            .getAll()
    }
}