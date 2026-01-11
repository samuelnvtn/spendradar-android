package com.pef.mendelu.novotnysamuel.spendradar

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.rule.GrantPermissionRule
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddAddItem
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemNamePrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemPricePrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemQuantityPrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddManualEntry
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddSaveButton
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddStoreName
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagListFab
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagStatsDay
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagStatsMonth
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagStatsWeek
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagStatsYear
import com.pef.mendelu.novotnysamuel.spendradar.ui.activities.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class UITestsStats {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_statsScreen(){
        composeRule.onNodeWithTag(TestTagListFab).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagListFab).performClick()

        composeRule.onNodeWithTag(TestTagAddManualEntry).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagAddManualEntry).performClick()

        composeRule.onNodeWithTag(TestTagAddStoreName).performTextInput("Test Store")
        composeRule.onNodeWithTag(TestTagAddAddItem).performClick()
        composeRule.onNodeWithTag(TestTagAddItemNamePrefix + "0").performTextInput("Test Item")
        composeRule.onNodeWithTag(TestTagAddItemQuantityPrefix + "0").performTextInput("1")
        composeRule.onNodeWithTag(TestTagAddItemPricePrefix + "0").performTextInput("25")
        composeRule.onNodeWithTag(TestTagAddItemPricePrefix + "0").performImeAction()
        composeRule.onNodeWithTag(TestTagAddSaveButton).performScrollTo()
        composeRule.onNodeWithTag(TestTagAddSaveButton).performClick()

        val statsTitle = composeRule.activity.getString(R.string.stats_title)
        composeRule.onNodeWithText(statsTitle).performClick()

        assertRangeLabel(TestTagStatsDay, composeRule.activity.getString(R.string.stats_total_day))
        assertRangeLabel(TestTagStatsWeek, composeRule.activity.getString(R.string.stats_total_week))
        assertRangeLabel(TestTagStatsMonth, composeRule.activity.getString(R.string.stats_total_month))
        assertRangeLabel(TestTagStatsYear, composeRule.activity.getString(R.string.stats_total_year))
    }

    private fun assertRangeLabel(tabTag: String, expectedLabel: String) {
        composeRule.onNodeWithTag(tabTag).performClick()
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(expectedLabel)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}
