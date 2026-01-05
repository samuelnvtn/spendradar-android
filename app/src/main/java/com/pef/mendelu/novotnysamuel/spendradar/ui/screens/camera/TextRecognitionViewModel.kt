package com.pef.mendelu.novotnysamuel.spendradar.ui.screens.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.pef.mendelu.novotnysamuel.spendradar.communication.CommunicationError
import com.pef.mendelu.novotnysamuel.spendradar.communication.CommunicationResult
import com.pef.mendelu.novotnysamuel.spendradar.communication.GeminiParser
import com.pef.mendelu.novotnysamuel.spendradar.communication.IGeminiRemoteRepository
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptCategory
import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.add.AddReceiptItemInput
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptDraft
import com.pef.mendelu.novotnysamuel.spendradar.model.ReceiptDraftStore
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
    private val geminiRepository: IGeminiRemoteRepository
) : ViewModel() {

    private val _isCapturing = MutableStateFlow(false)
    val isCapturing = _isCapturing.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<CommunicationError?>(null)
    val error = _error.asStateFlow()

    private val _navigateToEditId = MutableStateFlow<Long?>(null)
    val navigateToEditId = _navigateToEditId.asStateFlow()

    fun onImageCaptured(bitmap: Bitmap) {
        if (!_isCapturing.value) return
        _isCapturing.value = false
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            when (val result = geminiRepository.parseReceipt(bitmap)) {
                is CommunicationResult.Success -> {
                    val draft = mapToDraft(result.data)
                    ReceiptDraftStore.set(draft)
                    _navigateToEditId.value = 0L
                }
                is CommunicationResult.Error -> {
                    _error.value = result.error
                }
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

    fun onErrorHandled() {
        _error.value = null
    }

    private fun mapToDraft(parsed: GeminiParser.GeminiReceiptResponse): ReceiptDraft {
        val items = (parsed.items ?: emptyList()).mapNotNull { item ->
            val name = item.name?.trim().orEmpty()
            val quantity = item.quantity ?: 1
            val price = item.price ?: return@mapNotNull null
            if (name.isBlank() || quantity <= 0 || price < 0) return@mapNotNull null
            AddReceiptItemInput(
                name = name,
                quantity = quantity.toString(),
                price = formatPriceForInput((price * 100).roundToLong())
            )
        }
        val totalFromItems = items.sumOf { input ->
            val q = input.quantity.trim().toIntOrNull() ?: 0
            val p = parsePriceToCents(input.price) ?: 0
            p * q
        }
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

        return ReceiptDraft(
            storeName = parsed.storeName?.trim().orEmpty().ifBlank { "Unknown store" },
            dateMillis = timestamp.toEpochMilli(),
            time = timestamp.atZone(ZoneId.systemDefault()).toLocalTime(),
            location = location,
            category = category,
            items = items,
            totalPrice = totalPrice,
            currency = parsed.currency?.ifBlank { "CZK" } ?: "CZK"
        )
    }

    private fun parsePriceToCents(value: String): Long? {
        val normalized = value.trim().replace(",", ".")
        if (normalized.isBlank()) return null
        return try {
            val decimal = java.math.BigDecimal(normalized)
            decimal.multiply(java.math.BigDecimal(100))
                .setScale(0, java.math.RoundingMode.HALF_UP)
                .toLong()
        } catch (_: NumberFormatException) {
            null
        }
    }

    private fun formatPriceForInput(value: Long): String {
        val absValue = kotlin.math.abs(value)
        val crowns = absValue / 100
        val cents = absValue % 100
        return String.format(java.util.Locale("cs", "CZ"), "%d,%02d", crowns, cents)
    }
}
