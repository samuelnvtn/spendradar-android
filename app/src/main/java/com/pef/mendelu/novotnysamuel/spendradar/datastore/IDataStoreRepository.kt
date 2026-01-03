package com.pef.mendelu.novotnysamuel.spendradar.datastore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {
    suspend fun setFirstRun()
    suspend fun getFirstRun(): Boolean

    fun getLanguage(): Flow<String>
    suspend fun setLanguage(language: String)
}