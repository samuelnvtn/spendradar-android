package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: IReceiptsLocalRepository) : ViewModel() {

    private val _detailUIState: MutableStateFlow<DetailUIState> =
        MutableStateFlow(value = DetailUIState.Loading)

    val detailUIState = _detailUIState.asStateFlow()

    fun loadReceipt(id: Long) {
        viewModelScope.launch {
            _detailUIState.update { DetailUIState.Loading }
            val receipt = repository.getReceiptById(id)
            if (receipt == null) {
                _detailUIState.update { DetailUIState.Error("Receipt not found") }
            } else {
                _detailUIState.update { DetailUIState.Success(receipt) }
            }
        }
    }

    fun deleteReceipt(receipt: Receipt){
        viewModelScope.launch {
            repository.deleteReceipt(receipt)
            _detailUIState.update { DetailUIState.Deleted }
        }
    }

}
