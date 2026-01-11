package com.pef.mendelu.novotnysamuel.spendradar

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagBottomNavStats
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagListFab
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagPlaceholderList
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagPlaceholderStats
import com.pef.mendelu.novotnysamuel.spendradar.ui.activities.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class UITestPlaceholder {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_placeholders(){
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag(TestTagPlaceholderList)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule.onNodeWithTag(TestTagPlaceholderList).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTagBottomNavStats).performClick()

        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag(TestTagPlaceholderStats)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule.onNodeWithTag(TestTagPlaceholderStats).assertIsDisplayed()
    }
}
