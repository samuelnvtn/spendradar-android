package com.pef.mendelu.novotnysamuel.spendradar.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Singleton

@Singleton
class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {
    override suspend fun setFirstRun() {
        val preferencesKey = booleanPreferencesKey(DataStoreConstants.FIRST_RUN)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = false
        }
    }

    override suspend fun getFirstRun(): Boolean {
        return try {
            val preferencesKey = booleanPreferencesKey(DataStoreConstants.FIRST_RUN)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey))
                true
            else
                preferences[preferencesKey]!!
        }catch (e: Exception){
            e.printStackTrace()
            true
        }
    }

    override fun getLanguage(): Flow<String> {
        val defaultLang = Locale.getDefault().language
        return context.dataStore.data
            .map { prefs ->
                prefs[DataStoreConstants.LANGUAGE] ?: defaultLang
            }
    }

    override suspend fun setLanguage(code: String) {
        context.dataStore.edit { prefs ->
            prefs[DataStoreConstants.LANGUAGE] = code
        }
    }
}