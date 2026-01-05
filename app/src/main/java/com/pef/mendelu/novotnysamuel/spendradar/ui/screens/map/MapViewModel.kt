package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel  @Inject constructor(private val repository: IReceiptsLocalRepository) : ViewModel() {

    private val _mapUIState: MutableStateFlow<MapUIState> =
        MutableStateFlow(value = MapUIState.Loading)

    val mapUIState = _mapUIState.asStateFlow()

    fun loadReceipts(){
        viewModelScope.launch {
            _mapUIState.update { MapUIState.Loading }
            repository.getAllReceipts().collect { receipts ->
                _mapUIState.update {
                    MapUIState.Success(receipts)
                }
            }
        }
    }

}
