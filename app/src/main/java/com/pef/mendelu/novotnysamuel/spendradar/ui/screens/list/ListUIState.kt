package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.list

import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt

sealed class ListUIState {
    class Default: ListUIState()
    class Success(
        val receipts: List<Receipt>,
        val monthlyTotal: Long
    ) : ListUIState()
}
