package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: IReceiptsLocalRepository) : ViewModel() {

    private val _listUIState: MutableStateFlow<ListUIState> =
        MutableStateFlow(value = ListUIState.Default())

    val listUIState = _listUIState.asStateFlow()

    fun loadReceipts(){
        viewModelScope.launch {
            repository.getAllReceipts().collect { receipts ->
                val now = ZonedDateTime.now()
                val monthlyTotal = receipts
                    .filter { receipt ->
                        val date = receipt.timestamp.atZone(ZoneId.systemDefault())
                        date.year == now.year && date.month == now.month
                    }
                    .sumOf { it.totalPrice }
                _listUIState.update {
                    ListUIState.Success(receipts, monthlyTotal)
                }
            }
        }
    }
}
