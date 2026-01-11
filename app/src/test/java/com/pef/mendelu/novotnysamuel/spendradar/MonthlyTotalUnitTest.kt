package com.pef.mendelu.novotnysamuel.spendradar

import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.list.ListUIState
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.list.ListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

//pocita spravne monthly total? jen z uctenek v danem mesici?

@OptIn(ExperimentalCoroutinesApi::class)
class MonthlyTotalUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun monthlyTotalCorrect() = runTest {
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val receipts = listOf(
            buildReceipt("Store A", 300L, now.minusDays(1)),
            buildReceipt("Store B", 700L, now.minusMonths(2))
        )
        val repository = FakeReceiptsRepository(MutableStateFlow(receipts))
        val viewModel = ListViewModel(repository)

        viewModel.loadReceipts()
        advanceUntilIdle()

        val state = viewModel.listUIState.value
        assertTrue(state is ListUIState.Success)
        val successState = state as ListUIState.Success
        assertEquals(300L, successState.monthlyTotal)
    }

    private fun buildReceipt(store: String, total: Long, date: ZonedDateTime): Receipt {
        return Receipt(
            id = null,
            storeName = store,
            type = null,
            latitude = 0.0,
            longitude = 0.0,
            timestamp = date.toInstant(),
            totalPrice = total,
            currency = "CZK",
            items = emptyList()
        )
    }

    private class FakeReceiptsRepository(
        private val receiptsFlow: MutableStateFlow<List<Receipt>>
    ) : IReceiptsLocalRepository {
        override fun getAllReceipts(): Flow<List<Receipt>> = receiptsFlow

        override suspend fun getReceiptById(id: Long): Receipt? {
            return receiptsFlow.value.firstOrNull { it.id == id }
        }

        override suspend fun insertReceipt(receipt: Receipt): Long {
            throw UnsupportedOperationException("Not needed for this test")
        }

        override suspend fun deleteReceipt(receipt: Receipt) {
            throw UnsupportedOperationException("Not needed for this test")
        }

        override suspend fun updateReceipt(receipt: Receipt) {
            throw UnsupportedOperationException("Not needed for this test")
        }
    }
}
