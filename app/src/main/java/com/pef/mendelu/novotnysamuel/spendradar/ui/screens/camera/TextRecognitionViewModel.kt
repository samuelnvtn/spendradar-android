package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.pef.mendelu.novotnysamuel.spendradar.database.IReceiptsLocalRepository
import com.pef.mendelu.novotnysamuel.spendradar.model.Receipt
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptItem
import com.pef.mendelu.novotnysamuel.spendradar.utils.GeminiParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.roundToLong

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    private val repository: IReceiptsLocalRepository
) : ViewModel() {

    private val _isCapturing = MutableStateFlow(false)
    val isCapturing = _isCapturing.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _navigateToEditId = MutableStateFlow<Long?>(null)
    val navigateToEditId = _navigateToEditId.asStateFlow()

    fun onImageCaptured(bitmap: Bitmap) {
        if (!_isCapturing.value) return
        _isCapturing.value = false
        _isLoading.value = true

        viewModelScope.launch {
            val parsedData = GeminiParser.parseReceipt(bitmap)
            if (parsedData != null) {
                val receipt = mapToReceipt(parsedData)
                val id = repository.insertReceipt(receipt)
                _navigateToEditId.value = id
            }
            _isLoading.value = false
        }
    }

    fun startCapture() {
        if (_isLoading.value) return
        _isCapturing.value = true
    }

    fun onNavigationHandled() {
        _navigateToEditId.value = null
    }

    private fun mapToReceipt(parsed: GeminiParser.GeminiReceiptResponse): Receipt {
        val items = parsed.items.mapNotNull { item ->
            val name = item.name?.trim().orEmpty()
            val quantity = item.quantity ?: 1
            val price = item.price ?: return@mapNotNull null
            if (name.isBlank() || quantity <= 0 || price < 0) return@mapNotNull null
            ReceiptItem(
                itemName = name,
                quantity = quantity,
                pricePerUnit = (price * 100).roundToLong()
            )
        }
        val totalFromItems = items.sumOf { it.pricePerUnit * it.quantity }
        val totalPrice = parsed.totalPrice?.let { (it * 100).roundToLong() }
            ?.takeIf { it > 0 } ?: totalFromItems

        val date = parsed.date?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        val time = parsed.time?.let { runCatching { LocalTime.parse(it) }.getOrNull() }
        val timestamp = LocalDateTime.of(
            date ?: LocalDate.now(),
            time ?: LocalTime.now()
        ).atZone(ZoneId.systemDefault()).toInstant()

        val category = ReceiptCategory.fromStorage(parsed.category)
        val location = LatLng(49.2098028, 16.6145219)

        return Receipt(
            storeName = parsed.storeName?.trim().orEmpty().ifBlank { "Unknown store" },
            type = category.storageValue,
            latitude = location.latitude,
            longitude = location.longitude,
            timestamp = timestamp,
            totalPrice = totalPrice,
            currency = parsed.currency?.ifBlank { "CZK" } ?: "CZK",
            items = items
        )
    }
}
