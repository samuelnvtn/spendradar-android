package com.pef.mendelu.novotnysamuel.spendradar

import android.graphics.Bitmap
import com.pef.mendelu.novotnysamuel.spendradar.communication.CommunicationResult
import com.pef.mendelu.novotnysamuel.spendradar.communication.GeminiParser
import com.pef.mendelu.novotnysamuel.spendradar.communication.IGeminiRemoteRepository
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptDraftStore
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.camera.TextRecognitionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime
import java.time.ZoneId

//vypocet total price? real timestamp? filtrovani a validace itemu? fallbacks? error handling?

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TextRecognitionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun onImageCaptured_mapsGeminiResponseToDraft() = runTest {
        ReceiptDraftStore.consume()
        val response = GeminiParser.GeminiReceiptResponse(
            storeName = " ",
            date = "2024-06-01",
            time = "12:30",
            category = ReceiptCategory.GROCERIES.storageValue,
            items = listOf(
                GeminiParser.GeminiReceiptItem(name = "Milk", quantity = 2, price = 10.5),
                GeminiParser.GeminiReceiptItem(name = " ", quantity = 1, price = 5.0),
                GeminiParser.GeminiReceiptItem(name = "Bread", quantity = 0, price = 2.0),
                GeminiParser.GeminiReceiptItem(name = "Cheese", quantity = 1, price = -1.0)
            ),
            totalPrice = null,
            currency = ""
        )
        val repository = FakeGeminiRepository(response)
        val viewModel = TextRecognitionViewModel(repository)
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        viewModel.startCapture()
        viewModel.onImageCaptured(bitmap)
        advanceUntilIdle()

        val draft = ReceiptDraftStore.consume()
        assertNotNull(draft)
        val expectedMillis = LocalDateTime
            .parse("2024-06-01T12:30:00")
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        requireNotNull(draft)
        assertEquals("Unknown store", draft.storeName)
        assertEquals(expectedMillis, draft.dateMillis)
        assertEquals(ReceiptCategory.GROCERIES, draft.category)
        assertEquals("CZK", draft.currency)
        assertEquals(1, draft.items.size)
        assertEquals("Milk", draft.items[0].name)
        assertEquals("2", draft.items[0].quantity)
        assertEquals("10,50", draft.items[0].price)
        assertEquals(2100L, draft.totalPrice)
        assertEquals(0L, viewModel.navigateToEditId.value)
        assertNull(viewModel.error.value)
    }

    private class FakeGeminiRepository(
        private val response: GeminiParser.GeminiReceiptResponse
    ) : IGeminiRemoteRepository {
        override suspend fun parseReceipt(
            image: Bitmap
        ): CommunicationResult<GeminiParser.GeminiReceiptResponse> {
            return CommunicationResult.Success(response)
        }
    }
}
