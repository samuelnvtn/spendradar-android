package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import kotlin.math.abs

fun formatCurrency(value: Long, currencySuffix: String): String {
    val absValue = abs(value)
    val crowns = absValue / 100
    val cents = absValue % 100
    return String.format("%d,%02d %s", crowns, cents, currencySuffix)
}
