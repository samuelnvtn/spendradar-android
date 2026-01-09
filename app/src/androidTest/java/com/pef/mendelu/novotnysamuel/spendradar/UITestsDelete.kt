package com.pef.mendelu.novotnysamuel.spendradar

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddAddItem
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemNamePrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemPricePrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddItemQuantityPrefix
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddManualEntry
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddSaveButton
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagAddStoreName
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagDetailDeleteButton
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagListFab
import com.pef.mendelu.novotnysamuel.spendradar.ui.activities.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.rule.GrantPermissionRule

@ExperimentalCoroutinesApi
@HiltAndroidTest
class UITestsDelete {

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
    fun test_delete_receipt_from_detail() {
        val storeName = "Delete Test ${System.currentTimeMillis()}"

        openManualEntry()
        fillAddForm(storeName, "Apple")
        composeRule.onNodeWithTag(TestTagAddSaveButton).performScrollTo()
        composeRule.onNodeWithTag(TestTagAddSaveButton).performClick()

        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(storeName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(storeName).performClick()

        composeRule.onNodeWithTag(TestTagDetailDeleteButton).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagDetailDeleteButton).performClick()

        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(storeName).fetchSemanticsNodes().isEmpty()
        }
    }

    private fun openManualEntry() {
        composeRule.onNodeWithTag(TestTagListFab).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagListFab).performClick()
        composeRule.onNodeWithTag(TestTagAddManualEntry).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagAddManualEntry).performClick()
    }

    private fun fillAddForm(storeName: String, itemName: String) {
        composeRule.onNodeWithTag(TestTagAddStoreName).performTextInput(storeName)
        composeRule.onNodeWithTag(TestTagAddAddItem).performClick()

        composeRule.onNodeWithTag(TestTagAddItemNamePrefix + "0").performTextInput(itemName)
        composeRule.onNodeWithTag(TestTagAddItemQuantityPrefix + "0").performTextInput("1")
        composeRule.onNodeWithTag(TestTagAddItemPricePrefix + "0").performTextInput("15")
        composeRule.onNodeWithTag(TestTagAddItemPricePrefix + "0").performImeAction()
    }
}
