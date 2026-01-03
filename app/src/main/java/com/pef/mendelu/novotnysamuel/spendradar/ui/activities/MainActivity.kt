package com.pef.mendelu.novotnysamuel.spendradar.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.pef.mendelu.novotnysamuel.spendradar.datastore.IDataStoreRepository
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.NavGraph
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.SpendRadarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: IDataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Spustíme sledování jazyka
        lifecycleScope.launch {
            repository.getLanguage().first().let { lang ->
                if (lang.isNotEmpty()) {
                    setLocale(lang)
                }
            }
        }
        enableEdgeToEdge()
        setContent {
            SpendRadarTheme {
                NavGraph(startDestination = Destination.ListScreen.route)
            }
        }
    }
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}

