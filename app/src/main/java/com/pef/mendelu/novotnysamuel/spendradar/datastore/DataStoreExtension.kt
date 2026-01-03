package com.pef.mendelu.novotnysamuel.spendradar.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

val Context.languageFlow: Flow<String>
    get() = dataStore.data
        .map { prefs -> prefs[stringPreferencesKey(DataStoreConstants.LANGUAGE)] ?: "en" }

suspend fun Context.setLanguage(language: String) {
    dataStore.edit { prefs ->
        prefs[stringPreferencesKey(DataStoreConstants.LANGUAGE)] = language
    }
}