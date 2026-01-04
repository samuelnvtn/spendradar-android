package com.pef.mendelu.novotnysamuel.spendradar.model

data class ReceiptItem(
    var itemName: String,
    var quantity: Int,
    var pricePerUnit: Long
)
