package com.pef.mendelu.novotnysamuel.spendradar.ui.activities

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.pef.mendelu.novotnysamuel.spendradar.datastore.DataStoreConstants
import com.pef.mendelu.novotnysamuel.spendradar.datastore.IDataStoreRepository
import com.pef.mendelu.novotnysamuel.spendradar.datastore.dataStore
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.NavGraph
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.SpendRadarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import java.util.Locale


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreRepository: IDataStoreRepository
    override fun attachBaseContext(newBase: Context) {
        val defaultLang = Locale.getDefault().language

        val langCode = runBlocking {
            val prefs = newBase.dataStore.data.first()
            prefs[DataStoreConstants.LANGUAGE] ?: defaultLang
        }
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isLocaleLoaded = false

        lifecycleScope.launch {
            val langCode = dataStoreRepository.getLanguage().first()

            val locale = Locale(langCode)
            Locale.setDefault(locale)

            resources.configuration.setLocale(locale)
            createConfigurationContext(resources.configuration)

            isLocaleLoaded = true
        }

        enableEdgeToEdge()
        setContent {
            SpendRadarTheme {
                NavGraph(startDestination = Destination.ListScreen.route)
            }
        }
    }
}

