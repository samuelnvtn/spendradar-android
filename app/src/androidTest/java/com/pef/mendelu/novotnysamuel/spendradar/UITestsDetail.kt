package com.pef.mendelu.novotnysamuel.spendradar

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
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
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagListFab
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagMapBottomSheetDetail
import com.pef.mendelu.novotnysamuel.spendradar.testing.TestTagMapOpenFirstReceipt
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
class UITestsDetail {

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
    fun test_detail_from_list_and_map() {
        val storeName = "Detail Test ${System.currentTimeMillis()}"

        openManualEntry()
        Thread.sleep(1000)
        fillAddForm(storeName, "Coffee")
        Thread.sleep(1000)
        composeRule.onNodeWithTag(TestTagAddSaveButton).performScrollTo()
        composeRule.onNodeWithTag(TestTagAddSaveButton).performClick()
        Thread.sleep(1000)
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(storeName).fetchSemanticsNodes().isNotEmpty()
        }
        Thread.sleep(1000)
        composeRule.onNodeWithText(storeName).performClick()
        composeRule.onNodeWithText(storeName).assertIsDisplayed()

        val mapLabel = composeRule.activity.getString(R.string.map)
        composeRule.onNodeWithContentDescription(mapLabel, useUnmergedTree = true)
            .performClick()
        Thread.sleep(1000)
        composeRule.onNodeWithTag(TestTagMapOpenFirstReceipt).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagMapOpenFirstReceipt).performClick()
        Thread.sleep(1000)
        composeRule.onNodeWithTag(TestTagMapBottomSheetDetail).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagMapBottomSheetDetail).performClick()
        Thread.sleep(1000)
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(storeName).fetchSemanticsNodes().isNotEmpty()
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
        composeRule.onNodeWithTag(TestTagAddItemPricePrefix + "0").performTextInput("40")
        composeRule.onNodeWithTag(TestTagAddItemPricePrefix + "0").performImeAction()
    }
}
