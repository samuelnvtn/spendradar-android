package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import com.pef.mendelu.novotnysamuel.spendradar.ui.elements.categoryColor
import kotlin.math.roundToInt
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(private val repository: IReceiptsLocalRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUIState())
    val uiState = _uiState.asStateFlow()
    private var cachedReceipts: List<Receipt> = emptyList()

    init {
        viewModelScope.launch {
            repository.getAllReceipts().collect { receipts ->
                cachedReceipts = receipts
                computeStats()
            }
        }
    }

    fun onRangeChange(range: StatsRange) {
        _uiState.update { it.copy(range = range) }
        computeStats()
    }

    private fun computeStats() {
        _uiState.update { it.copy(isLoading = true) }
        val filtered = filterByRange(cachedReceipts, _uiState.value.range)
        val summary = buildSummary(filtered)
        val pieItems = buildCategoryPie(filtered)
        _uiState.update {
            it.copy(
                summary = summary,
                pieItems = pieItems,
                isLoading = false
            )
        }
    }

    private fun filterByRange(receipts: List<Receipt>, range: StatsRange): List<Receipt> {
        val now = ZonedDateTime.now()
        return receipts.filter { receipt ->
            val date = receipt.timestamp.atZone(ZoneId.systemDefault())
            when (range) {
                StatsRange.DAY -> date.toLocalDate() == now.toLocalDate()
                StatsRange.WEEK -> {
                    val startOfWeek = now.toLocalDate().minusDays(((now.dayOfWeek.value + 6) % 7).toLong())
                    val endOfWeek = startOfWeek.plusDays(6)
                    val localDate = date.toLocalDate()
                    !localDate.isBefore(startOfWeek) && !localDate.isAfter(endOfWeek)
                }
                StatsRange.MONTH -> date.year == now.year && date.month == now.month
                StatsRange.YEAR -> date.year == now.year
            }
        }
    }

    private fun buildSummary(receipts: List<Receipt>): StatsSummary {
        if (receipts.isEmpty()) {
            return StatsSummary(0L, null, null)
        }
        val total = receipts.sumOf { it.totalPrice }
        val mostVisited = receipts
            .groupingBy { it.storeName }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
        val topSpend = receipts
            .groupBy { it.storeName }
            .mapValues { entry -> entry.value.sumOf { it.totalPrice } }
            .maxByOrNull { it.value }
            ?.key
        return StatsSummary(total, mostVisited, topSpend)
    }

    private fun buildCategoryPie(receipts: List<Receipt>): List<StatsPieItem> {
        val total = receipts.sumOf { it.totalPrice }
        if (total <= 0L) return emptyList()

        val grouped = receipts.groupBy { ReceiptCategory.fromStorage(it.type) }
        return grouped.entries.mapNotNull { (category, items) ->
            val sum = items.sumOf { it.totalPrice }
            if (sum <= 0L) {
                null
            } else {
                val percent = ((sum.toDouble() / total.toDouble()) * 100).roundToInt()
                StatsPieItem(
                    category = category,
                    value = sum.toDouble(),
                    color = categoryColor(category),
                    percent = percent
                )
            }
        }.sortedByDescending { it.value }
    }

}
