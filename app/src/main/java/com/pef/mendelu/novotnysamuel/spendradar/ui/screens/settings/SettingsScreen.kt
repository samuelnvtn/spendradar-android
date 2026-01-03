package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.settings

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.pef.mendelu.novotnysamuel.spendradar.R
import com.pef.mendelu.novotnysamuel.spendradar.navigation.Destination
import com.pef.mendelu.novotnysamuel.spendradar.navigation.INavigationRouter
import com.pef.mendelu.novotnysamuel.spendradar.ui.activities.MainActivity
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BaseScreen
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.BottomNavBar
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.lightButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.theme.textColor
import java.util.Locale


@Composable
fun SettingsScreen(
    navigation: INavigationRouter
){
    val viewModel: SettingsViewModel = hiltViewModel()
    val language by viewModel.language.collectAsState()

    BaseScreen(
        bottomBarContent = {
            BottomNavBar(
                selectedDestination = Destination.ListScreen,
                navigation = navigation
            )
        },
        placeholderScreenContent = null,
        topBarText = (stringResource(R.string.settings)),
        onBackClick = {
            navigation.returnBack()
        },
        content = {
            language?.let { it1 ->
                SettingsScreenContent(
                    paddingValues = it,
                    navigation = navigation,
                    viewModel = viewModel,
                    language = it1,
                )
            }
        }
    )
}

fun updateLocale(context: Context, languageCode: String) {
    val appLocales = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(appLocales)
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    viewModel: SettingsViewModel,
    language: String,
){
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf(stringResource(R.string.cz) to "cs", stringResource(R.string.en) to "en")
    val context = LocalContext.current
    val activity = context as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.lang),
                color = textColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            Box(
                modifier = Modifier
                    .width(250.dp)
                    .border(1.dp, lightButton, RoundedCornerShape(80.dp))
                    .clickable { expanded = true }
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = languages.find { it.second == language }?.first ?: language,
                    color = textColor,
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(250.dp)
                ) {
                    languages
                        .filter { it.second != language }
                        .forEach { (label, code) ->
                            DropdownMenuItem(
                                text = { Text(label, textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()) },
                                onClick = {
                                    viewModel.setLanguage(code)
                                    updateLocale(
                                        context,
                                        code
                                    )
                                    expanded = false
                                    activity?.recreate()
                                }
                            )
                        }
                }
            }
            VersionFooter()
        }
    }
}

@Composable
fun VersionFooter() {
    val context = LocalContext.current
    val version = remember {
        try {
            val pkgInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pkgInfo.versionName ?: "?"
        } catch (e: Exception) {
            "?"
        }
    }

    Text(
        text = stringResource(R.string.author) + " Samuel Novotný",
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, top = 32.dp),
        textAlign = TextAlign.Center,
        color = textColor
    )

    Text(
        text = "v$version",
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        textAlign = TextAlign.Center,
        color = textColor
    )
}