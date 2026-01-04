package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: IReceiptsLocalRepository
) : ViewModel(), AddActions {

    private val _uiState = MutableStateFlow(AddUIState())
    val uiState = _uiState.asStateFlow()

    override fun onStoreNameChange(value: String) {
        _uiState.update { it.copy(storeName = value) }
    }

    override fun onDateChange(value: Long?) {
        _uiState.update { it.copy(dateMillis = value) }
    }

    override fun onTimeChange(value: LocalTime) {
        _uiState.update { it.copy(time = value) }
    }

    override fun onLocationChange(value: LatLng) {
        _uiState.update { it.copy(location = value) }
    }

    override fun onCategoryChange(category: ReceiptCategory) {
        _uiState.update { it.copy(category = category) }
    }

    override fun onAddItem() {
        _uiState.update { it.copy(items = it.items + AddReceiptItemInput()) }
    }

    override fun onRemoveItem(index: Int) {
        _uiState.update { state ->
            if (index !in state.items.indices) {
                state
            } else {
                val updated = state.items.toMutableList()
                updated.removeAt(index)
                state.copy(items = updated)
            }
        }
    }

    override fun onItemNameChange(index: Int, value: String) {
        updateItem(index) { it.copy(name = value) }
    }

    override fun onItemQuantityChange(index: Int, value: String) {
        updateItem(index) { it.copy(quantity = value) }
    }

    override fun onItemPriceChange(index: Int, value: String) {
        updateItem(index) { it.copy(price = value) }
    }

    override fun onSave() {
        val state = _uiState.value
        if (state.isSaving) return

        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val items = state.items.mapNotNull { input ->
                val name = input.name.trim()
                if (name.isBlank()) return@mapNotNull null
                val quantity = input.quantity.trim().toIntOrNull() ?: return@mapNotNull null
                val price = parsePriceToCents(input.price) ?: return@mapNotNull null
                ReceiptItem(
                    itemName = name,
                    quantity = quantity,
                    pricePerUnit = price
                )
            }

            val totalPrice = items.sumOf { it.pricePerUnit * it.quantity }
            val timestamp = resolveTimestamp(state.dateMillis, state.time)

            val receipt = Receipt(
                storeName = state.storeName.trim().ifBlank { "Unknown store" },
                type = state.category.storageValue,
                latitude = state.location.latitude,
                longitude = state.location.longitude,
                timestamp = timestamp,
                totalPrice = totalPrice,
                currency = "CZK",
                items = items
            )

            repository.insertReceipt(receipt)
            Log.d("AddViewModel", "Saved receipt: $receipt")
            _uiState.update { it.copy(isSaving = false, saveCompleted = true) }
        }
    }

    override fun onSaveHandled() {
        _uiState.update { it.copy(saveCompleted = false) }
    }

    private fun updateItem(index: Int, block: (AddReceiptItemInput) -> AddReceiptItemInput) {
        _uiState.update { state ->
            val items = state.items.toMutableList()
            if (index in items.indices) {
                items[index] = block(items[index])
            }
            state.copy(items = items)
        }
    }

    private fun resolveTimestamp(dateMillis: Long?, time: LocalTime): Instant {
        val date = if (dateMillis != null) {
            Instant.ofEpochMilli(dateMillis).atZone(ZoneId.systemDefault()).toLocalDate()
        } else {
            LocalDateTime.now().toLocalDate()
        }
        return LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant()
    }

    private fun parsePriceToCents(value: String): Long? {
        val normalized = value.trim()
            .replace(",", ".")
        if (normalized.isBlank()) return null
        return try {
            val decimal = BigDecimal(normalized)
            decimal.multiply(BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).toLong()
        } catch (ex: NumberFormatException) {
            null
        }
    }
}
