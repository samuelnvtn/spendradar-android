package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.stats

import androidx.compose.ui.graphics.Color
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory

enum class StatsRange {
    DAY,
    WEEK,
    MONTH,
    YEAR
}

data class StatsPieItem(
    val category: ReceiptCategory,
    val value: Double,
    val color: Color,
    val percent: Int
)

data class StatsSummary(
    val totalSpent: Long,
    val mostVisitedStore: String?,
    val topSpendStore: String?
)

data class StatsUIState(
    val range: StatsRange = StatsRange.MONTH,
    val summary: StatsSummary = StatsSummary(0L, null, null),
    val pieItems: List<StatsPieItem> = emptyList(),
    val isLoading: Boolean = true,
    val hasAnyData: Boolean = false
)
