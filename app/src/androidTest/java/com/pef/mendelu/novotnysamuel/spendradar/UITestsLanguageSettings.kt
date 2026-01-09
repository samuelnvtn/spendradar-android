package com.pef.mendelu.novotnysamuel.spendradar

import android.content.res.Configuration
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagNavToSettings
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagSettingsLanguage
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagSettingsLanguageDropdown
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagSettingsLanguageButton
import com.pef.mendelu.novotnysamuel.spendradar.ui.activities.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.Locale

@ExperimentalCoroutinesApi
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsLanguageSettings {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_languageSettings() {
        composeRule.onNodeWithTag(TestTagNavToSettings).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagNavToSettings).performClick()

        val targetLocale = if (Locale.getDefault().language == "cs") {
            Locale("en")
        } else {
            Locale("cs")
        }
        val targetConfig = Configuration(composeRule.activity.resources.configuration)
        targetConfig.setLocale(targetLocale)
        val targetContext = composeRule.activity.createConfigurationContext(targetConfig)
        val expectedLabel = targetContext.getString(R.string.lang)

        composeRule.onNodeWithTag(TestTagSettingsLanguageDropdown).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagSettingsLanguageDropdown).performClick()
        composeRule.onNodeWithTag(TestTagSettingsLanguageButton).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagSettingsLanguageButton).performClick()

        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(expectedLabel)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule.onNodeWithTag(TestTagSettingsLanguage).assertTextEquals(expectedLabel)
    }
}
