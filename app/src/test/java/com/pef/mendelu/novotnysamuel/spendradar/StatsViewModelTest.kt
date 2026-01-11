package com.pef.mendelu.novotnysamuel.spendradar

import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.stats.StatsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

//pocita spravne monthly summary pro mesicni range?

@OptIn(ExperimentalCoroutinesApi::class)
class StatsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun monthlySummary_aggregatesCurrentMonthTotals() = runTest {
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val receipts = listOf(
            buildReceipt("Store A", 100L, now.minusDays(1), ReceiptCategory.GROCERIES),
            buildReceipt("Store A", 50L, now.minusDays(3), ReceiptCategory.FOOD_DRINK),
            buildReceipt("Store B", 200L, now.minusDays(2), ReceiptCategory.ELECTRONICS),
            buildReceipt("Store C", 999L, now.minusMonths(1), ReceiptCategory.OTHER)
        )
        val repository = FakeReceiptsRepository(MutableStateFlow(receipts))
        val viewModel = StatsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(350L, state.summary.totalSpent)
        assertEquals("Store A", state.summary.mostVisitedStore)
        assertEquals("Store B", state.summary.topSpendStore)
    }

    private fun buildReceipt(
        store: String,
        total: Long,
        date: ZonedDateTime,
        category: ReceiptCategory
    ): Receipt {
        return Receipt(
            id = null,
            storeName = store,
            type = category.storageValue,
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
