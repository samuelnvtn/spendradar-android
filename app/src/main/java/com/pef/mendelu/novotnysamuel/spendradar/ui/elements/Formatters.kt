package com.pef.mendelu.novotnysamuel.spendradar.ui.elements

import java.util.Locale
import kotlin.math.abs

fun formatCurrency(value: Long): String {
    val absValue = abs(value)
    val crowns = absValue / 100
    val cents = absValue % 100
    return String.format(Locale("cs", "CZ"), "%d,%02d Kč", crowns, cents)
}
