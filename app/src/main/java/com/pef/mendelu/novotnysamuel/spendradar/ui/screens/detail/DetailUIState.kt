package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.detail

import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt

sealed class DetailUIState {
    data object Loading : DetailUIState()
    data class Success(val receipt: Receipt) : DetailUIState()
    data object Deleted : DetailUIState()
    data class Error(val message: String? = null) : DetailUIState()
}
