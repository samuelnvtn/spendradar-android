package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.map

import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt

sealed class MapUIState {
    data object Loading : MapUIState()
    class Success(val receipts: List<Receipt>) : MapUIState()
}
