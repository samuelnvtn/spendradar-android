package com.pef.mendelu.novotnysamuel.spendradar.datastore

interface IDataStoreRepository {
    suspend fun setFirstRun()
    suspend fun getFirstRun(): Boolean
}