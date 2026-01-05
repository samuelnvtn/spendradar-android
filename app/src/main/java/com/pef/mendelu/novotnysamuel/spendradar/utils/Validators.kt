package com.pef.mendelu.novotnysamuel.spendradar.utils

import com.pef.mendelu.novotnysamuel.spendradar.ui.screens.add.AddReceiptItemInput

fun isValidQuantity(value: String): Boolean {
    val quantity = value.trim().toIntOrNull() ?: return false
    return quantity > 0
}

fun isValidPrice(value: String): Boolean {
    val normalized = value.trim().replace(",", ".")
    if (normalized.isBlank()) return false
    return try {
        val decimal = java.math.BigDecimal(normalized)
        decimal.multiply(java.math.BigDecimal(100))
            .setScale(0, java.math.RoundingMode.HALF_UP)
            .toLong() > 0
    } catch (ex: NumberFormatException) {
        false
    }
}

fun areItemsValid(items: List<AddReceiptItemInput>): Boolean {
    if (items.isEmpty()) return false
    return items.all { item ->
        item.name.trim().isNotBlank() &&
                isValidQuantity(item.quantity) &&
                isValidPrice(item.price)
    }
}