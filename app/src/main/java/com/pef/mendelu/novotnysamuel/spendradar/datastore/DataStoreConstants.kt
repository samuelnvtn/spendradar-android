package com.pef.mendelu.novotnysamuel.spendradar.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreConstants {
    const val FIRST_RUN = "first_run"
    val LANGUAGE = stringPreferencesKey("language")
}