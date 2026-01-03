package com.pef.mendelu.novotnysamuel.spendradar.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SpendRadarTheme {
                NavGraph(startDestination = Destination.ListScreen.route)
            }
        }
    }
}

